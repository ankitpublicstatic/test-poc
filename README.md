# test-poc

# CI/CD Pipeline Guide for Java Spring Boot Application

This project demonstrates a complete CI/CD ecosystem for a Java Spring Boot application with multiple deployment pipelines:

Docker (multi-stage & simple builds)

Jenkins Pipeline (AWS ECR + Kubernetes deployment)

GitHub Actions CI/CD

GCP Cloud Build + Cloud Run deployment

Kubernetes deployment (GKE/EKS)

## This guide explains how to build, deploy, and automate the application using all modern CI/CD tools.

📁 Repository Structure
/
├── docker/
│   ├── option-a-multistage/
│   │     └── Dockerfile
│   ├── option-b-simple/
│   │     └── Dockerfile
│
├── cicd/
│   ├── github-actions/
│   │     └── deploy.yml
│   ├── jenkins/
│   │     └── Jenkinsfile
│   ├── cloudbuild.yaml
│
├── k8s/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── hpa.yaml
│   └── configmap.yaml
│
└── README.md

# 🚀 1. Build & Run Using Docker
Option A – Multi-Stage Build (Recommended)
docker build -t test-poc:multi -f docker/multistage/Dockerfile .
docker run -p 8080:8080 test-poc:multi

docker run --hostname=0c64e76ece3c --mac-address=4e:81:e6:67:24:60 --env=PATH=/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin --env=JAVA_HOME=/opt/java/openjdk --env=LANG=en_US.UTF-8 --env=LANGUAGE=en_US:en --env=LC_ALL=en_US.UTF-8 --env=JAVA_VERSION=jdk-17.0.17+10 --network=bridge -p 8080:8080 --restart=no --label='org.opencontainers.image.ref.name=ubuntu' --label='org.opencontainers.image.version=24.04' --runtime=runc -d test-poc:multi

Option B – Simple Runtime Dockerfile
mvn clean package -DskipTests
docker build -t test-poc:simple -f docker/simple/Dockerfile .
docker run -p 8080:8080 test-poc:simple

# 🧩 2. GitHub Actions CI/CD Pipeline

File: cicd/github-actions/deploy.yml

This workflow:

Builds JAR

Builds Docker image

Pushes to AWS ECR

Deploys to Kubernetes (optional)

Trigger
on:
  push:
    branches:
      - main

GitHub Secrets Required
| Secret                | Description                                       |
| --------------------- | ------------------------------------------------- |
| AWS_ACCESS_KEY_ID     | AWS IAM user key                                  |
| AWS_SECRET_ACCESS_KEY | AWS IAM secret                                    |
| AWS_REGION            | e.g., ap-south-1                                  |
| ECR_URI               | `<aws-account>.dkr.ecr.region.amazonaws.com/repo` |

# 🏗 3. Jenkins CI/CD Pipeline

File: cicd/jenkins/Jenkinsfile

### Stages included:

| Stage                | Description              |
| -------------------- | ------------------------ |
| Checkout             | Downloads repo           |
| Build                | `mvn clean package`      |
| Test                 | Runs JUnit tests         |
| AWS ECR Login        | CLI-based authentication |
| Docker Build         | Build image              |
| Docker Push          | Push to ECR              |
| Deploy to Kubernetes | Runs kubectl apply       |


Jenkins Requirements
	Plugins to install:
	
		Pipeline
		
		Docker Pipeline
		
		AWS Credentials
		
		Kubernetes CLI

Checkout source code

Build JAR

Run unit tests

Login to AWS ECR

Build Docker image

Push to ECR

Deploy to Kubernetes using kubectl

Required Jenkins Plugins

Pipeline

Docker Pipeline

AWS Credentials

Kubernetes CLI


### Jenkins Credentials
| ID                    | Type                | Purpose        |
| --------------------- | ------------------- | -------------- |
| aws-access-key-id     | Secret text         | Access key     |
| aws-secret-access-key | Secret text         | Secret key     |
| kubeconfig            | Secret file         | For Kubernetes |
| dockerhub-creds       | Username + Password | For DockerHub  |

# ☁️ 4. Deploying to AWS ECR Manually
### Login to AWS ECR
aws ecr get-login-password --region ap-south-1 \
| docker login --username AWS --password-stdin <REGISTRY_URL>

### Build & Push Image
docker build -t test-poc .
docker tag test-poc:latest <REGISTRY_URL>/test-poc:latest
docker push <REGISTRY_URL>/test-poc:latest

#☁️ 5. Deploying Using GCP Cloud Build + Cloud Run

File: cicd/cloudbuild.yaml
This workflow:

✔ Builds JAR
✔ Builds Docker image
✔ Pushes to GCR
✔ Deploys to Cloud Run automatically

This pipeline:

Runs Maven build

Builds Docker image

Pushes to Google Container Registry

Deploys to Cloud Run

Trigger manually:
gcloud builds submit --config=cicd/cloudbuild.yaml .

#☸️ 6. Kubernetes Deployment (GKE or EKS)
###Apply manifests
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/hpa.yaml
kubectl apply -f k8s/configmap.yaml

###Check rollout status
kubectl get pods
kubectl get svc
kubectl describe deployment test-poc

#🧪 7. Local Development

Run application locally:

./mvnw spring-boot:run


Run tests:

./mvnw test

# 🛡 8. Security Best Practices

Use OWASP Dependency Check

Enable SonarQube code quality scans

Follow OWASP Top 10

Use IAM least privilege for CI/CD

Avoid committing secrets

Use Kubernetes Secrets / AWS KMS / GCP Secret Manager

🧾 9. Environment Variables

Examples:

SPRING_PROFILES_ACTIVE=prod
DB_HOST=localhost
LOG_LEVEL=INFO


Configured in:

Jenkinsfile

Dockerfile

Kubernetes ConfigMap

GitHub Secrets

Cloud Run

🎯 10. Summary of All CI/CD Options
| Platform       | Build | Test | Docker | Push | Deploy |
| -------------- | ----- | ---- | ------ | ---- | ------ |
| GitHub Actions | ✔     | ✔    | ✔      | ✔    | ✔      |
| Jenkins        | ✔     | ✔    | ✔      | ✔    | ✔      |
| AWS ECR        | –     | –    | ✔      | ✔    | –      |
| GCP Cloud Run  | ✔     | ✔    | ✔      | ✔    | ✔      |
| Kubernetes     | –     | –    | –      | –    | ✔      |

