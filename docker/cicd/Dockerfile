FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:resolve
COPY src src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar","app.jar"  ]