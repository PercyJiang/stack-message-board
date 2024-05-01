resource "aws_rds_cluster" "msg_rds_cluster" {
  count = local.deploy_rds ? 1 : 0

  # id
  cluster_identifier = var.rds_cluster_identifier
  database_name      = var.rds_dbname

  # spec
  engine              = var.rds_engine
  engine_version      = var.rds_engine_version
  availability_zones  = var.az_list
  skip_final_snapshot = true

  # master db user
  master_username = "${var.rds_dbname}_master_username"
  master_password = "${var.rds_dbname}_master_password"
}

resource "aws_rds_cluster_instance" "msg_rds_instance" {
  count      = local.deploy_rds ? 1 : 0
  depends_on = [aws_rds_cluster.msg_rds_cluster]

  # id
  identifier         = var.rds_instance_identifier
  cluster_identifier = aws_rds_cluster.msg_rds_cluster[0].id

  # spec
  engine         = var.rds_engine
  engine_version = var.rds_engine_version
  instance_class = var.rds_instance_class
}
