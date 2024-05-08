####
#### iam
####
resource "aws_iam_role" "percy_ec2_role" {
  count = local.deploy_ecs ? 1 : 0

  name               = "percy_ec2_role"
  path               = "/system/"
  assume_role_policy = data.aws_iam_policy_document.assume_role_policy.json
}
resource "aws_iam_role_policy_attachment" "AmazonECS_FullAccess" {
  count      = local.deploy_ecs ? 1 : 0
  depends_on = [aws_iam_role.percy_ec2_role]

  role       = aws_iam_role.percy_ec2_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonECS_FullAccess"
}
resource "aws_iam_role_policy_attachment" "AmazonEC2ContainerServiceForEC2Role" {
  count      = local.deploy_ecs ? 1 : 0
  depends_on = [aws_iam_role.percy_ec2_role]

  role       = aws_iam_role.percy_ec2_role[0].name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}
resource "aws_iam_instance_profile" "percy_ecs_instance_profile" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_iam_role.percy_ec2_role,
    aws_iam_role_policy_attachment.AmazonECS_FullAccess,
    aws_iam_role_policy_attachment.AmazonEC2ContainerServiceForEC2Role
  ]

  name = "percy_ecs_instance_profile"
  role = aws_iam_role.percy_ec2_role[0].name
}

####
#### sg
####
resource "aws_security_group" "percy_ecs_sg" {
  count = local.deploy_ecs ? 1 : 0

  # id
  name = "percy_ecs_sg"

  # conn
  vpc_id = var.vpc_id

  # allow all traffic
  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # allow all traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# launch template
resource "aws_launch_template" "percy_ec2_launch_template" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_iam_instance_profile.percy_ecs_instance_profile,
    aws_security_group.percy_ecs_sg,
    aws_ecs_cluster.percy_ecs_cluster
  ]

  # id
  name = "percy_ec2_launch_template"

  # spec
  image_id      = var.ec2_ami_id
  instance_type = var.ec2_instance_type

  # required for ecs manage
  iam_instance_profile {
    name = aws_iam_instance_profile.percy_ecs_instance_profile[0].name
  }

  # conn ecs
  user_data = base64encode(data.cloudinit_config.ec2init.rendered)

  # required for ssh
  vpc_security_group_ids = [aws_security_group.percy_ecs_sg[0].id]
  key_name               = var.ec2_rsa_key_name
}

# asg
resource "aws_autoscaling_group" "percy_asg" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_launch_template.percy_ec2_launch_template,
    aws_ecs_cluster.percy_ecs_cluster
  ]

  # id
  name = "percy_asg"

  # conn ec2
  launch_template {
    id      = aws_launch_template.percy_ec2_launch_template[0].id
    version = "$Latest"
  }
  tag {
    key                 = "Name"
    value               = "percy_instance_name"
    propagate_at_launch = true
  }
  tag {
    key                 = "AmazonECSManaged" # required for ecs manage
    value               = true
    propagate_at_launch = true
  }

  # spec
  availability_zones = slice(var.az_list, 0, 3)
  desired_capacity   = 1
  max_size           = 1
  min_size           = 1
}
