output "order_processing_queue_url" {
  description = "SQS queue url for order processing"
  value = module.order_queue.queue_url
}

output "order_table_name" {
  value = module.order_table.dynamodb_table_id
}

output "is_local" {
  value = var.is_local
}