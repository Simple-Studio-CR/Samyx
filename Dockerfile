FROM openjdk:8-jre
VOLUME /tmp
ADD ./target/API-MH-1.0.0.RELEASE.war samyx.jar
ENTRYPOINT ["java", "-war", "samyx.jar"]
