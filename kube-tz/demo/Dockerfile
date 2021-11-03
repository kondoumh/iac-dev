FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine as builder

ADD src/ src/
ADD gradle/ gradle/
ADD build.gradle .
ADD gradlew .
ADD settings.gradle .
RUN ./gradlew build

FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine as runner

COPY --from=builder /build/libs/demo-0.0.1-SNAPSHOT.jar /demo-0.0.1-SNAPSHOT.jar 

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
