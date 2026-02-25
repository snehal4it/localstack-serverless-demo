# Config
TF_BACKEND_STATE_KEY=lambda/terraform.tfstate
REGION=us-east-1

# Local Specific
TF_BACKEND_LOCAL_BUCKET=local-terraform-state
LOCAL_ENDPOINT=http://localhost:4566

TF_BACKEND_AWS_BUCKET=order-processing-bucket

.PHONY: bootstrap-localstack teardown-localstack init-tf-local deploy-tf-local init-tf-aws deploy-tf-aws

# Create the S3 bucket in LocalStack for state storage
bootstrap-localstack: teardown-localstack
	@docker-compose up -d
	@sleep 5
	# backend state is stored in s3 and it must exists before terraform command is run
	# s3 bucket for backend is used by terraform and not managed by terraform
	AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test \
	aws s3api create-bucket --bucket $(TF_BACKEND_LOCAL_BUCKET) --region us-east-1 \
    		--endpoint-url http://localhost:4566 \
            --no-verify-ssl || true

teardown-localstack:
	@docker-compose down || true

# Init Terraform specifically for LocalStack
init-tf-local: bootstrap-localstack
	terraform init -reconfigure \
    		-backend-config="bucket=$(TF_BACKEND_LOCAL_BUCKET)" \
    		-backend-config="key=$(TF_BACKEND_STATE_KEY)" \
    		-backend-config="region=$(REGION)" \
    		-backend-config="access_key=test" \
            -backend-config="secret_key=test" \
    		-backend-config="endpoints={s3=\"$(LOCAL_ENDPOINT)\",sts=\"$(LOCAL_ENDPOINT)\",iam=\"$(LOCAL_ENDPOINT)\"}" \
    		-backend-config="skip_credentials_validation=true" \
    		-backend-config="skip_metadata_api_check=true" \
    		-backend-config="skip_requesting_account_id=true" \
    		-backend-config="use_path_style=true"

deploy-tf-local: init-tf-local
	TF_VAR_is_local=true terraform validate
	TF_VAR_is_local=true terraform apply -auto-approve


# --- REAL AWS FLOW ---
# Note: No 'skip' variables or 'endpoints' are passed here.
# Terraform will use standard AWS SDK logic.
init-tf-aws:
	terraform init -reconfigure \
		-backend-config="bucket=$(TF_BACKEND_AWS_BUCKET)" \
		-backend-config="key=$(TF_BACKEND_STATE_KEY)" \
		-backend-config="region=$(REGION)"

deploy-tf-aws: init-tf-aws
	terraform validate
	terraform apply -auto-approve