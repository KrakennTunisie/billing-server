# 1. Build Stage
FROM eclipse-temurin:17-jre-jammy as build
WORKDIR /app


COPY pom.xml .
COPY src ./src


RUN mvn clean package -DskipTests

# 2. Run Stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/billing-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761

ENTRYPOINT [ "java","-jar","/app/app.jar" ]
