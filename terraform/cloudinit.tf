data "cloudinit_config" "ec2init_ecs" {
  gzip          = false
  base64_encode = true

  part {
    content_type = "text/x-shellscript"
    content      = templatefile("scripts/ec2init_ecs.sh", {})
  }
}

data "cloudinit_config" "ec2init_eks" {
  gzip          = false
  base64_encode = true

  part {
    content_type = "text/x-shellscript"
    content      = templatefile("scripts/ec2init_eks.sh", {})
  }
}
