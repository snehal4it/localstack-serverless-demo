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
- How to define AWS clients that use LocalStack instead of real AWS
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

#### Stop LocalStack
Once localstack is stopped, all resources will **not** be persisted and next time it will start with clean state
There is a way to persist resources, please refer official Localstack document

```bash
    make cleanup-localstack
```

### AWS
Make sure you have created AWS account and local environment is already setup to use AWS account

#### Deploy application in AWS

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