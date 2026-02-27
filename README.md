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

This demo showcases:

- How to configure a Spring Boot project to point against LocalStack endpoints
- How to define AWS clients that use LocalStack instead of real AWS
- How to interact locally with S3, queues, etc.
- Local-first development workflows

👍 Lightweight example codebase  
⚠ Not production-grade Spring Boot code  
⚠ Focused on LocalStack usage only

---

## 🛠 Prerequisites

Ensure you have the following installed:

- Docker (to run LocalStack locally)
- Java 17 SDK
- Maven

---

## 🚀 Run the Demo

### 1. Start LocalStack (Docker)

```bash
make deploy-localstack

