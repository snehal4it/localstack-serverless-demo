# Config
TF_BACKEND_STATE_KEY=lambda/terraform.tfstate
REGION=us-east-1

# Local Specific
TF_BACKEND_LOCAL_BUCKET=local-terraform-state
LOCAL_ENDPOINT=http://localhost:4566

# if you get error related with bucket exists, rename following value as s3 buckets are global
TF_BACKEND_AWS_BUCKET=order-processing-bucket-localstack11111

.PHONY: bootstrap-localstack teardown-localstack init-tf-local deploy-tf-local init-tf-aws deploy-tf-aws bootstrap-aws destroy-aws-env

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
bootstrap-aws:
	# s3 bucket for backend is used by terraform and not managed by terraform
	# so it must be created and managed outside of terraform
	aws s3api create-bucket --bucket $(TF_BACKEND_AWS_BUCKET) --region us-east-1 || true

init-tf-aws: bootstrap-aws
	terraform init -reconfigure \
		-backend-config="bucket=$(TF_BACKEND_AWS_BUCKET)" \
		-backend-config="key=$(TF_BACKEND_STATE_KEY)" \
		-backend-config="region=$(REGION)"

deploy-tf-aws: init-tf-aws
	terraform validate
	terraform apply -auto-approve

destroy-aws-env:
	rm -f tf_destroy_plan
	terraform plan -destroy -out tf_destroy_plan
	terraform apply --auto-approve tf_destroy_plan
	aws s3api delete-object --bucket $(TF_BACKEND_AWS_BUCKET) --key $(TF_BACKEND_STATE_KEY) --region us-east-1
	aws s3api delete-bucket --bucket $(TF_BACKEND_AWS_BUCKET) --region us-east-1