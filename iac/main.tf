terraform {
  required_version = ">= 1.10.0"

  # partial configuration
  backend "s3" { }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.33.0"
    }
  }
}