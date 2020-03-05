FROM maven:3.6.1-jdk-8-alpine as compile
COPY . /home/
WORKDIR /home/
RUN mvn install

FROM openjdk:8-jre-alpine
WORKDIR /home/
COPY --from=compile "/home/target/challenge-0.0.1-SNAPSHOT.jar" .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "-Dpython.import.site=false", "/home/challenge-0.0.1-SNAPSHOT.jar"]