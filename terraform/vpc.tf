data "aws_caller_identity" "current" {}
data "aws_region" "current" {}
data "aws_vpcs" "current" {}

data "aws_security_groups" "default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpcs.current.ids[0]]
  }
  filter {
    name   = "group-name"
    values = ["default"]
  }
}

data "aws_subnets" "percy_subnets_ab" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpcs.current.ids[0]]
  }
  filter {
    name   = "availability-zone"
    values = slice(var.az_list, 0, 2)
  }
}
