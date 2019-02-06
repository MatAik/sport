FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/cityservice.jar
ADD ${JAR_FILE} cityservice.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","cityservice.jar"]