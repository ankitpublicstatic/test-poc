#FROM openjdk:17-alpine
FROM eclipse-temurin:17-jre
ARG JAR_FILE=target/test-poc-1.0.0.jar
COPY ${JAR_FILE} app.jar
#EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]