resource "aws_ecs_cluster" "msg_ecs_cluster" {
  count = local.deploy_ecs ? 1 : 0

  name = "msg_ecs_cluster"

  setting {
    name  = "containerInsights"
    value = "enabled"
  }
}

resource "aws_ecs_task_definition" "msg_ecs_task_definition" {
  count      = local.deploy_ecs ? 1 : 0
  depends_on = [aws_rds_cluster.msg_rds_cluster]

  family = "msg_ecs_task_family"
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
          value = aws_rds_cluster.msg_rds_cluster[0].endpoint
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

resource "aws_ecs_service" "msg_ecs_service" {
  count = local.deploy_ecs ? 1 : 0
  depends_on = [
    aws_rds_cluster.msg_rds_cluster,
    aws_ecs_cluster.msg_ecs_cluster,
    aws_ecs_task_definition.msg_ecs_task_definition,
    aws_autoscaling_group.msg_asg
  ]

  # id
  name            = "msg_ecs_service"
  cluster         = aws_ecs_cluster.msg_ecs_cluster[0].id
  task_definition = aws_ecs_task_definition.msg_ecs_task_definition[0].arn

  # spec
  launch_type   = "EC2"
  desired_count = var.ecs_task_desired_count
}
