####
#### iam
####
resource "aws_iam_role" "percy_eks_role" {
  count = local.deploy_eks ? 1 : 0

  name               = "percy_eks_role"
  assume_role_policy = data.aws_iam_policy_document.assume_role_policy.json
}
resource "aws_iam_role_policy_attachment" "AmazonEKSClusterPolicy" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_role]

  role       = aws_iam_role.percy_eks_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}
resource "aws_iam_role_policy_attachment" "AmazonEKSVPCResourceController" {
  count      = local.deploy_eks ? 1 : 0
  depends_on = [aws_iam_role.percy_eks_role]

  role       = aws_iam_role.percy_eks_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
}

# cluster
resource "aws_eks_cluster" "percy_eks_cluster" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_role,
    aws_iam_role_policy_attachment.AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.AmazonEKSVPCResourceController
  ]

  # id
  name = "percy_eks_cluster"

  # iam
  role_arn = aws_iam_role.percy_eks_role[0].arn

  vpc_config {
    subnet_ids = data.aws_subnets.percy_subnets_ab.ids
  }
}

# node group
resource "aws_eks_node_group" "percy_eks_node_group" {
  count = local.deploy_eks ? 1 : 0
  depends_on = [
    aws_iam_role.percy_eks_role,
    aws_iam_role_policy_attachment.AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.AmazonEKSVPCResourceController,
    aws_eks_cluster.percy_eks_cluster
  ]

  # id
  node_group_name = "percy_eks_node_group"

  cluster_name  = aws_eks_cluster.percy_eks_cluster[0].name
  node_role_arn = aws_iam_role.percy_eks_role[0].arn
  subnet_ids    = data.aws_subnets.percy_subnets_ab.ids

  scaling_config {
    desired_size = 1
    max_size     = 2
    min_size     = 1
  }

  update_config {
    max_unavailable = 1
  }
}
