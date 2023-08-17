FROM openjdk:8-jdk-alpine
COPY target/API-MH-1.0.0.RELEASE.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar","/app.jar"]