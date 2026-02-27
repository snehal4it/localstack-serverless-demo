.PHONY: build deploy-localstack cleanup-localstack deploy-aws destroy-aws

# Build the JAR
build:
	./mvnw clean package

deploy-localstack: build
	cd iac && make deploy-tf-local

cleanup-localstack:
	cd iac && make teardown-localstack

deploy-aws: build
	cd iac && make deploy-tf-aws

run-e2e-test:
	SQS_URL=$(shell cd iac && terraform output -raw order_processing_queue_url); \
	ORDER_TABLE_NAME=$(shell cd iac && terraform output -raw order_table_name); \
	IS_LOCAL=$(shell cd iac && terraform output -raw is_local); \
	./mvnw verify -Dskip.surefire.tests -Dsqs.url=$$SQS_URL \
		-Dorder.table.name=$$ORDER_TABLE_NAME \
		-Dis.local=$$IS_LOCAL

destroy-aws:
	cd iac && make destroy-aws-env

