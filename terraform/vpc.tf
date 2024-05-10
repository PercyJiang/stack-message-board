data "aws_caller_identity" "current" {}
data "aws_region" "current" {}

data "aws_vpc" "default_vpc" { id = var.vpc_id }
data "aws_security_group" "default_security_group" {id = var.security_group_id }

data "aws_subnets" "percy_subnets_ab" {
  filter {
    name   = "vpc-id"
    values = [var.vpc_id]
  }
  filter {
    name   = "availability-zone"
    values = slice(var.az_list, 0, 2)
  }
}
