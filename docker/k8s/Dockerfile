# Goal -> Build Jar Inside Docker -> Produce small runtime image

# -----------------------------------------------------------
# STAGE 1: BUILD STAGE
# -----------------------------------------------------------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw clean package -DskipTests
	

# -----------------------------------------------------------
# STAGE 2: RUNTIME STAGE
# -----------------------------------------------------------
	
	
FROM eclipse-temurin:17-jre

ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]



# Build & Run

# docker build -t test-poc:latest .
# docker run -p 8080:8080 test-poc:latest

# Push image to docker hub for pulling image for k8s from docker hub when creating deployment object $kubectl apply -f k8s/deployment.yaml
# docker tag test-poc:latest ankitjava242/test-poc:1.0.0
# docker push ankitjava242/test-poc:1.0.0

#kubectl apply -f k8s/deployment.yaml
#kubectl apply -f k8s/service.yaml
#kubectl apply -f k8s/hpa.yaml
#kubectl apply -f k8s/configmap.yaml
 
#kubectl get pods
#kubectl get svc
#  kubectl describe deployment test-poc
#   kubectl get nodes -o wide > get node IP
#    kubectl delete pod -l app=test-poc > delete pod realted to test-poc app
#    kubectl apply -f docker/k8s/deployment.yaml
#    kubectl apply -f docker/k8s/deployment.yaml
 