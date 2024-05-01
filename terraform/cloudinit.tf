data "cloudinit_config" "ec2init" {
  gzip          = false
  base64_encode = true

  part {
    content_type = "text/x-shellscript"
    content      = templatefile("scripts/ec2init.sh", {})
  }
}
