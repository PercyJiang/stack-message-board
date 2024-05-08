locals {
  region_primary   = "us-east-1"
  region_secondary = "us-west-2"

  deploy_rds = false
  deploy_ecs = false
  deploy_eks = true
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
