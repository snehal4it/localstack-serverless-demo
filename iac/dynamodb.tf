module "order_table" {
  source  = "terraform-aws-modules/dynamodb-table/aws"
  version = "5.5.0"

  name     = "order"
  hash_key = "id"

  attributes = [
    {
      name = "id"
      type = "S" # String
    }
  ]

  billing_mode   = "PAY_PER_REQUEST"

  tags = {
    Component = "Database"
  }
}