variable "is_local" {
  type    = bool
  default = false
}

provider "aws" {
  region = "us-east-1"

  # use null for real AWS to allow environment-based auth
  access_key = var.is_local ? "test" : null
  secret_key = var.is_local ? "test" : null

  default_tags {
    tags = {
      Project     = "OrderProcessing"
      Owner       = "PlatformTeam"
      ManagedBy   = "Terraform"
      Environment = var.is_local ? "localstack" : "production"
    }
  }

  dynamic "endpoints" {
    for_each = var.is_local ? [1] : []
    content {
      lambda         = "http://localhost:4566"
      s3             = "http://localhost:4566"
      iam            = "http://localhost:4566"
      sts            = "http://localhost:4566"
      sqs            = "http://localhost:4566"
      dynamodb       = "http://localhost:4566"
      cloudwatch     = "http://localhost:4566"
      cloudwatchlogs = "http://localhost:4566"
    }
  }

  skip_credentials_validation = var.is_local
  skip_metadata_api_check     = var.is_local
  skip_requesting_account_id  = var.is_local
  s3_use_path_style           = var.is_local
}
