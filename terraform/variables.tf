# vpc
variable "vpc_id" { default = "vpc-08acce998dcaee4ad" }
variable "security_group_id" { default = "sg-0a09b7f00e697cecc" }
variable "az_list" { default = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1e", "us-east-1f"] }

# rds
variable "rds_dbname" { default = "percydb" }
variable "rds_cluster_identifier" { default = "percy-rds-cluster" }
variable "rds_instance_identifier" { default = "percy-rds-instance" }
variable "rds_instance_class" { default = "db.t3.medium" }
variable "rds_engine" { default = "aurora-postgresql" }
variable "rds_engine_version" { default = "15.4" }
variable "rds_port" { default = "5432" }

# ec2
variable "ec2_rsa_key_name" { default = "test_key_pair" }
variable "ec2_ami_id_ecs" { default = "ami-057f57c2fcd14e5f4" }
variable "ec2_instance_type_ecs" { default = "t2.small" }
variable "ec2_ami_id_eks" { default = "ami-049924d678af7a43b" }
variable "ec2_instance_type_eks" { default = "t2.micro" }

# ecs
variable "ecs_container_name" { default = "percy_container" }
variable "ecs_image_name" { default = "jiangpercy666/message-board-java:latest" }
variable "ecs_task_desired_count" { default = 1 }
