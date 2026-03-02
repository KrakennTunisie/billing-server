# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-17 As Build
WORKDIR /app


COPY pom.xml .
COPY src ./src


RUN mvn clean package -DskipTests

# 2. Run Stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/billing-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761

ENTRYPOINT [ "java","-jar","/app/app.jar" ]