FROM openjdk:11-jre
VOLUME /tmp
ADD ./target/API-MH-1.0.0.RELEASE.jar simplestudio-samyx.jar
ENTRYPOINT ["java", "-jar", "simplestudio-samyx.jar"]
