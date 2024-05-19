####
#### eks cluster role
####
resource "aws_iam_role" "percy_eks_cluster_role" {
  count = local.deploy_eks ? 1 : 0

  name               = "percy_eks_cluster_role"
  assume_role_policy = data.aws_iam_policy_document.assume_role_policy.json
}
resource "aws_iam_role_policy_attachment" "AmazonEKSClusterPolicy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_cluster_role]

  role       = aws_iam_role.percy_eks_cluster_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}
resource "aws_iam_role_policy_attachment" "AmazonEKSServicePolicy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_cluster_role]

  role       = aws_iam_role.percy_eks_cluster_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSServicePolicy"
}
resource "aws_iam_role_policy_attachment" "AmazonEKSVPCResourceController" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_cluster_role]

  role       = aws_iam_role.percy_eks_cluster_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
}

# eks cluster
resource "aws_eks_cluster" "percy_eks_cluster" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_cluster_role,
    aws_iam_role_policy_attachment.AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.AmazonEKSVPCResourceController
  ]

  # id
  name = "percy_eks_cluster"

  # iam
  role_arn = aws_iam_role.percy_eks_cluster_role[0].arn

  vpc_config {
    subnet_ids = data.aws_subnets.percy_subnets_ab.ids
  }
}

####
#### eks cluster addon
####
resource "aws_eks_addon" "percy_eks_addon_kube_proxy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_eks_cluster.percy_eks_cluster]

  cluster_name  = aws_eks_cluster.percy_eks_cluster[0].name
  addon_name    = "kube-proxy"
  addon_version = "v1.29.3-eksbuild.2"
}
resource "aws_eks_addon" "percy_eks_addon_vpc_cni" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_eks_cluster.percy_eks_cluster]

  cluster_name  = aws_eks_cluster.percy_eks_cluster[0].name
  addon_name    = "vpc-cni"
  addon_version = "v1.18.1-eksbuild.3"
}

####
#### eks worker node role
####
resource "aws_iam_role" "percy_eks_worker_node_role" {
  count = local.deploy_eks ? 1 : 0

  name               = "percy_eks_worker_node_role"
  assume_role_policy = data.aws_iam_policy_document.assume_role_policy.json
}
resource "aws_iam_role_policy_attachment" "AmazonEKSWorkerNodePolicy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
}
resource "aws_iam_role_policy_attachment" "AmazonEKS_CNI_Policy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
}
resource "aws_iam_role_policy_attachment" "AmazonEC2ContainerRegistryReadOnly" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}
resource "aws_iam_role_policy_attachment" "AmazonSSMManagedInstanceCore" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}
resource "aws_iam_policy" "eks_ebs_csi_driver_policy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  name   = "eks_ebs_csi_driver_policy"
  policy = file("${path.module}/policies/eks-ebs-csi-driver-policy.json")
}
resource "aws_iam_role_policy_attachment" "eks_ebs_csi_driver_policy" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_worker_node_role,
    aws_iam_policy.eks_ebs_csi_driver_policy
  ]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = aws_iam_policy.eks_ebs_csi_driver_policy[0].arn
}
resource "aws_iam_policy" "eks_aws_lb_controller_policy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  name   = "eks_aws_lb_controller_policy"
  policy = file("${path.module}/policies/eks-aws-lb-controller-policy.json")
}
resource "aws_iam_role_policy_attachment" "eks_aws_lb_controller_policy" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_worker_node_role,
    aws_iam_policy.eks_aws_lb_controller_policy
  ]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = aws_iam_policy.eks_aws_lb_controller_policy[0].arn
}
resource "aws_iam_policy" "eks_ecr_policy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_worker_node_role]

  name   = "eks_ecr_policy"
  policy = file("${path.module}/policies/eks-ecr-policy.json")
}
resource "aws_iam_role_policy_attachment" "eks_ecr_policy" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_worker_node_role,
    aws_iam_policy.eks_ecr_policy
  ]

  role       = aws_iam_role.percy_eks_worker_node_role[0].name
  policy_arn = aws_iam_policy.eks_ecr_policy[0].arn
}
resource "aws_iam_instance_profile" "percy_eks_worker_node_instance_profile" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_worker_node_role,
    aws_iam_role_policy_attachment.AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.AmazonEC2ContainerRegistryReadOnly,
    aws_iam_role_policy_attachment.AmazonSSMManagedInstanceCore,
    aws_iam_role_policy_attachment.eks_ebs_csi_driver_policy,
    aws_iam_role_policy_attachment.eks_aws_lb_controller_policy,
    aws_iam_role_policy_attachment.eks_ecr_policy
  ]

  name = "percy_eks_worker_node_instance_profile"
  role = aws_iam_role.percy_eks_worker_node_role[0].name
}

# ec2 launch template for eks node group
resource "aws_launch_template" "percy_ec2_launch_template_eks_node_group" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_eks_cluster.percy_eks_cluster]

  # id
  name = "percy_ec2_launch_template_eks_noce_group"

  # spec
  image_id = var.ec2_ami_id_eks

  # conn eks
  user_data = base64encode(data.cloudinit_config.ec2init_eks.rendered)

  # required for ssh
  vpc_security_group_ids = [var.security_group_id]
  key_name               = var.ec2_rsa_key_name
}

# node group
resource "aws_eks_node_group" "percy_eks_node_group" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_eks_cluster.percy_eks_cluster,
    aws_eks_addon.percy_eks_addon_kube_proxy,
    aws_eks_addon.percy_eks_addon_vpc_cni,
    aws_iam_role.percy_eks_worker_node_role,
    aws_iam_role_policy_attachment.AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.AmazonEC2ContainerRegistryReadOnly,
    aws_iam_role_policy_attachment.AmazonSSMManagedInstanceCore,
    aws_iam_role_policy_attachment.eks_ebs_csi_driver_policy,
    aws_iam_role_policy_attachment.eks_aws_lb_controller_policy,
    aws_iam_role_policy_attachment.eks_ecr_policy,
    aws_launch_template.percy_ec2_launch_template_eks_node_group,
  ]

  # id
  node_group_name = "percy_eks_node_group"

  # conn
  cluster_name  = aws_eks_cluster.percy_eks_cluster[0].name
  node_role_arn = aws_iam_role.percy_eks_worker_node_role[0].arn
  subnet_ids    = data.aws_subnets.percy_subnets_ab.ids

  # conn ec2
  launch_template {
    id      = aws_launch_template.percy_ec2_launch_template_eks_node_group[0].id
    version = "$Latest"
  }

  tags = {
    "Name" = "percy_instance_name_eks"
  }

  scaling_config {
    desired_size = 1
    max_size     = 2
    min_size     = 0
  }

  update_config {
    max_unavailable = 2
  }
}
