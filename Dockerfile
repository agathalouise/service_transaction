FROM eclipse-temurin:17-jdk-alpine
COPY target/service_transaction-*.jar service_transaction.jar
ENTRYPOINT ["java", "-jar", "service_transaction.jar"]
