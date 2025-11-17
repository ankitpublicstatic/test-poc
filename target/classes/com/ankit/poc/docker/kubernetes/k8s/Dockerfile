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
RUN .mvnw clean package -DskipTests
	

# -----------------------------------------------------------
# STAGE 2: RUNTIME STAGE
# -----------------------------------------------------------
	
	
FROM eclipse-temurin:17-jre

ARG JAR_FILE = /app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]



# Build & Run

# docker build -t test-poc:multi .
# docker run -p 8080:8080 test-poc:multi

