FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine

COPY build/libs/demo-0.0.1-SNAPSHOT.jar demo-0.0.1-SNAPSHOT.jar 

EXPOSE 18080

ENTRYPOINT [ "java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
