# Use this when your JAR is already built
FROM eclipse-temurin:17-jre

ARG JAR_FILE=target/test-poc-1.0.0.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app.jar" ]      

# Build & Run
# mvn clean package -DskipTests
# docker build -t test-poc:simple .
# docker run -p 8080:8080 test-poc:simple

