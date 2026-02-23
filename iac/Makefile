# Config
STATE_KEY=lambda/terraform.tfstate
REGION=us-east-1
JAR=../target/order-processing-0.0.1-SNAPSHOT-aws.jar

# Local Specific
LOCAL_BUCKET=local-terraform-state
LOCAL_ENDPOINT=http://localhost:4566

AWS_BUCKET=order-processing-bucket

.PHONY: build bootstrap-local init-local apply-local deploy-aws local-down

# Build the JAR
build:
	cd ../ && mvn clean package

# Create the S3 bucket in LocalStack for state storage
bootstrap-local: local-down
	@docker-compose up -d
	@sleep 5
	AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test \
	aws s3api create-bucket --bucket $(LOCAL_BUCKET) --region us-east-1 \
    		--endpoint-url http://localhost:4566 \
            --no-verify-ssl || true

local-down:
	docker-compose down

# Init Terraform specifically for LocalStack
init-local: bootstrap-local
	terraform init -reconfigure \
    		-backend-config="bucket=$(LOCAL_BUCKET)" \
    		-backend-config="key=$(STATE_KEY)" \
    		-backend-config="region=$(REGION)" \
    		-backend-config="access_key=test" \
            -backend-config="secret_key=test" \
    		-backend-config="endpoints={s3=\"$(LOCAL_ENDPOINT)\",sts=\"$(LOCAL_ENDPOINT)\",iam=\"$(LOCAL_ENDPOINT)\"}" \
    		-backend-config="skip_credentials_validation=true" \
    		-backend-config="skip_metadata_api_check=true" \
    		-backend-config="skip_requesting_account_id=true" \
    		-backend-config="use_path_style=true"

apply-local:
	TF_VAR_is_local=true terraform apply -auto-approve

# One command to rule them all (Local)
local: init-local apply-local

# --- REAL AWS FLOW ---
# Note: No 'skip' variables or 'endpoints' are passed here.
# Terraform will use standard AWS SDK logic.
aws-init:
	terraform init -reconfigure \
		-backend-config="bucket=$(AWS_BUCKET)" \
		-backend-config="key=$(STATE_KEY)" \
		-backend-config="region=$(REGION)"

deploy-aws: build
	#terraform apply -auto-approve