locals {
  account_number = "992382707788"

  region_primary   = "us-east-1"
  region_secondary = "us-west-2"

  deploy_rds = true
  deploy_ecs = true
}

provider "aws" {
  region = local.region_primary
}
provider "aws" {
  alias  = "replica"
  region = local.region_secondary
}

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
