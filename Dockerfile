FROM openjdk:8-jre
VOLUME /tmp
ADD ./target/servicing-0.0.1-SNAPSHOT.jar jireh-servicing.jar
ENTRYPOINT ["java", "-jar", "jireh-servicing.jar"]
