resource "aws_s3_bucket" "jiangpercy_test_bucket" {
  count = 0

  # id
  bucket = "jiangpercy-test-bucket" # name must be global unique
}
