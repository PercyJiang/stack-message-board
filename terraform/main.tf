locals {
  region_primary   = "us-east-1"
  region_secondary = "us-west-2"

  deploy_rds = true
  deploy_ecs = true
  deploy_eks = false
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
