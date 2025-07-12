FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY ../target/metadata-1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]