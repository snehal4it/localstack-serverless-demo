module "order_queue" {
  source  = "terraform-aws-modules/sqs/aws"
  version = "5.2.1"

  name = "order-processing-queue"

  # 3 attempts before DLQ
  create_dlq = true
  redrive_policy = {
    maxReceiveCount = 3
  }

  # Ensure visibility timeout > Lambda timeout (90s)
  visibility_timeout_seconds = 100
}

# trigger remains a raw resource as it's the "glue" between modules
resource "aws_lambda_event_source_mapping" "sqs_trigger" {
  event_source_arn = module.order_queue.queue_arn
  function_name    = module.lambda_function.lambda_function_arn
  enabled          = true
  batch_size       = 1
}