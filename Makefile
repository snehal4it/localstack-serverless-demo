.PHONY: build deploy-localstack cleanup-localstack deploy-aws

# Build the JAR
build:
	./mvnw clean package

deploy-localstack: build
	cd iac && make deploy-tf-local

cleanup-localstack:
	cd iac && make teardown-localstack

deploy-aws: build
	cd iac && make deploy-tf-aws

