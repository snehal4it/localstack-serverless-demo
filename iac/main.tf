module "lambda_function" {
  source  = "terraform-aws-modules/lambda/aws"
  version = "8.7.0"

  function_name = "spring-cloud-function-demo"
  handler       = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
  runtime       = "java17"
  memory_size   = 512
  timeout       = 90

  # Path to the 'Shaded' JAR built by your Maven plugin
  create_package         = false
  local_existing_package = "${path.module}/../target/order-processing-0.0.1-SNAPSHOT-aws.jar"

  environment_variables = {
    SPRING_CLOUD_FUNCTION_DEFINITION = "sqs-order-handler-function"
  }

  tags = {
    "additional-tag": "test"
  }

  # Permissions to consume from the queue created in sqs.tf
  attach_policy_statements = true
  policy_statements = {
    sqs_consume = {
      effect    = "Allow"
      actions   = ["sqs:ReceiveMessage", "sqs:DeleteMessage", "sqs:GetQueueAttributes"]
      resources = [module.order_queue.queue_arn]
    }
  }
}