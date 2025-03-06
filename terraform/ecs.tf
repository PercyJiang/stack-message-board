# ecs cluster
resource "aws_ecs_cluster" "percy_ecs_cluster" {
  count = local.deploy_ecs ? 1 : 0

  name = "percy_ecs_cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

# ecs task definition
resource "aws_ecs_task_definition" "percy_ecs_task_definition" {
  count      = local.deploy_ecs ? 1 : 0
  depends_on = [aws_rds_cluster.percy_rds_cluster]

  family = "percy_ecs_task_family"
  container_definitions = jsonencode([
    {
      name      = var.ecs_container_name
      image     = var.ecs_image_name
      cpu       = 1024
      memory    = 1024
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          appProtocol   = "http"
        }
      ]
      environment = [
        {
          name  = "DB_HOST"
          value = aws_rds_cluster.percy_rds_cluster[0].endpoint
        },
        {
          name  = "DB_PORT"
          value = var.rds_port
        },
        {
          name  = "DB_NAME"
          value = var.rds_dbname
        },
        {
          name  = "DB_USERNAME"
          value = "${var.rds_dbname}_master_username"
        },
        {
          name  = "DB_PASSWORD"
          value = "${var.rds_dbname}_master_password"
        },
      ]
    }
  ])
}

####
#### ec2 role for ecs
####
resource "aws_iam_role" "percy_ec2_role_ecs" {
  count = local.deploy_ecs ? 1 : 0

  name               = "percy_ec2_role_ecs"
  path               = "/system/"
  assume_role_policy = data.aws_iam_policy_document.assume_role_policy.json
}
resource "aws_iam_role_policy_attachment" "AmazonECS_FullAccess" {
  count      = local.deploy_ecs ? 1 : 0
  depends_on = [aws_iam_role.percy_ec2_role_ecs]

  role       = aws_iam_role.percy_ec2_role_ecs[0].name
  policy_arn = "arn:aws:iam::aws:policy/AmazonECS_FullAccess"
}
resource "aws_iam_role_policy_attachment" "AmazonEC2ContainerServiceForEC2Role" {
  count      = local.deploy_ecs ? 1 : 0
  depends_on = [aws_iam_role.percy_ec2_role_ecs]

  role       = aws_iam_role.percy_ec2_role_ecs[0].name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}
resource "aws_iam_instance_profile" "percy_ecs_instance_profile" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_iam_role.percy_ec2_role_ecs,
    aws_iam_role_policy_attachment.AmazonECS_FullAccess,
    aws_iam_role_policy_attachment.AmazonEC2ContainerServiceForEC2Role
  ]

  name = "percy_ecs_instance_profile"
  role = aws_iam_role.percy_ec2_role_ecs[0].name
}

# ec2 launch template
resource "aws_launch_template" "percy_ec2_launch_template_ecs" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_iam_instance_profile.percy_ecs_instance_profile,
    aws_ecs_cluster.percy_ecs_cluster
  ]

  # id
  name = "percy_ec2_launch_template_ecs"

  # spec
  image_id      = var.ec2_ami_id_ecs
  instance_type = var.ec2_instance_type_ecs

  # required for ecs manage
  iam_instance_profile {
    name = aws_iam_instance_profile.percy_ecs_instance_profile[0].name
  }

  # conn ecs
  user_data = base64encode(data.cloudinit_config.ec2init_ecs.rendered)

  # required for ssh
  vpc_security_group_ids = data.aws_security_groups.default.ids
  key_name               = var.ec2_rsa_key_name
}

# ec2 asg
resource "aws_autoscaling_group" "percy_asg_ecs" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_launch_template.percy_ec2_launch_template_ecs,
    aws_ecs_cluster.percy_ecs_cluster
  ]

  # id
  name = "percy_asg_ecs"

  # conn ec2
  launch_template {
    id      = aws_launch_template.percy_ec2_launch_template_ecs[0].id
    version = "$Latest"
  }
  tag {
    key                 = "Name"
    value               = "percy_instance_name_ecs"
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

# ecs service
resource "aws_ecs_service" "percy_ecs_service" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_rds_cluster.percy_rds_cluster,
    aws_ecs_cluster.percy_ecs_cluster,
    aws_ecs_task_definition.percy_ecs_task_definition,
    aws_autoscaling_group.percy_asg_ecs
  ]

  # id
  name            = "percy_ecs_service"
  cluster         = aws_ecs_cluster.percy_ecs_cluster[0].id
  task_definition = aws_ecs_task_definition.percy_ecs_task_definition[0].arn

  # spec
  launch_type   = "EC2"
  desired_count = var.ecs_task_desired_count
}
