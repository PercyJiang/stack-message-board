# vpc
variable "vpc_id" { default = "vpc-08acce998dcaee4ad" }
variable "az_list" { default = ["us-east-1a", "us-east-1b", "us-east-1c"] }

# rds
variable "rds_dbname" { default = "msgdb" }
variable "rds_cluster_identifier" { default = "msg-rds-cluster" }
variable "rds_instance_identifier" { default = "msg-rds-instance" }
variable "rds_instance_class" { default = "db.t3.medium" }
variable "rds_engine" { default = "aurora-postgresql" }
variable "rds_engine_version" { default = "15.4" }
variable "rds_port" { default = "5432" }

# ec2
variable "ec2_ami_id" { default = "ami-0d66a7f8b40cf4f5b" }
variable "ec2_instance_type" { default = "t4g.small" }
variable "ec2_rsa_key_name" { default = "test_key_pair" }

# ecs
variable "ecs_container_name" { default = "msg_container" }
variable "ecs_image_name" { default = "jiangpercy666/docker-java-jar:latest" }
variable "ecs_task_desired_count" { default = 1 }
