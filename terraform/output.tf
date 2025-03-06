output "vpc_ids" {
  value = data.aws_vpcs.current.ids
}

output "security_group_ids" {
  value = data.aws_security_groups.default.ids
}
