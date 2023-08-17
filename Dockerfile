FROM openjdk:8-jre-slim
COPY ./target/API-MH-1.0.0.RELEASE.jar API-MH-1.0.0.RELEASE.jar
EXPOSE 8080
RUN ls -l
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar", "API-MH-1.0.0.RELEASE.jar"]
