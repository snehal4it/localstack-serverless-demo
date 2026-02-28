# LocalStack Serverless Demo

A small example project for **evaluating LocalStack use cases** with a Java / Spring Boot application.  
This repository demonstrates how to develop and test AWS-integrated serverless workloads locally using **LocalStack**.

> 🧪 **Important:**  
> This code intentionally illustrates LocalStack capabilities. It is **not intended to be a production-quality AWS application** or a reference architecture for enterprise deployments.

---

## 🧠 What is LocalStack?

LocalStack is a local AWS cloud stack emulator that allows you to run AWS services on your local machine using Docker. It enables developers to:

- develop and test AWS applications locally
- avoid deploying to actual AWS for every change
- eliminate cloud costs during development
- iterate rapidly without real cloud credentials

LocalStack supports a broad set of AWS services including S3, SQS, DynamoDB, Lambda, API Gateway and more.

---

## 📌 Repository Purpose


AWS Lambda gets triggered by message in SQS queue, message is then parsed and stored in AWS Dynamo db.

This demo showcases:

- How to configure AWS Lambda using Spring cloud functions and do the local development using localstack
- How to define AWS clients that use LocalStack for local development using endpoint override so that same code works for real AWS account also
- How to configure Terraform (Infrastructure as code) such that same setup can be used for provisioning resources locally with localstack as well as in real AWS environment.
- How to write end-to-end test which can run locally against localstack as well as in AWS environment. 

👍 Lightweight example codebase  
⚠ Not production-grade Spring Boot code  
⚠ Focused on LocalStack usage only

---

## 🛠 Prerequisites

Ensure you have the following installed:

- Docker (to run LocalStack locally)
- Java 17 SDK (JAVA_HOME env variable set)
- Localstack
- AWS account (for deployment in AWS cloud)
- AWS CLI

No need of `tflocal` or other wrapper from localstack

---

## 🚀 Run the Demo
Run following commands from root of repository/source

### Localstack
#### Deploy application using Localstack

```bash
    make deploy-localstack
```

#### Run End-to-End test against LocalStack
This step assumes that application is already deployed to localstack

```bash
    make run-e2e-test
```

#### Run AWS CLI commands with endpoint override
- This step assumes that application is already deployed to localstack
- Uses actual aws cli commands with endpoint override

```bash
    # only works for pointing to localstack
    export AWS_ACCESS_KEY_ID=test
    export AWS_SECRET_ACCESS_KEY=test
    export AWS_REGION=us-east-1
    export AWS_ENDPOINT_URL=http://localhost:4566
    
    # send message to sqs with endpoint override (deployed in localstack)
    aws sqs send-message \
        --queue-url http://localhost:4566/000000000000/order-processing-queue \
        --message-body "{\"id\": \"1234567890\", \"status\": \"NEW\"}" \
        --endpoint-url http://localhost:4566
        
    # check contents of dynamo db table with endpoint override (deployed in localstack)
    aws dynamodb scan --table-name order --endpoint-url http://localhost:4566 --limit 10
    
```



#### Stop LocalStack
Once localstack is stopped, all resources will **not** be persisted and next time it will start with clean state
There is a way to persist resources, please refer official Localstack document

```bash
    make cleanup-localstack
```

### AWS
Make sure you have created AWS account and local environment is already setup to use AWS account

#### Deploy application in AWS
Note: If you encounter error with s3 bucket name while deploying to real AWS account, please rename bucket name in `iac/Makefile` in source repository because in aws cloud, bucket names are global, and it's possible that somebody might have already used that name

```bash
    make deploy-aws
```

#### Run End-to-End test against AWS
This step assumes that application is already deployed to AWS

```bash
    make run-e2e-test
```



#### Remove/Destroy application from AWS

```bash
    make destroy-aws
```