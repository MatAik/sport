FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/calculatorservice.jar
ADD ${JAR_FILE} calculatorservice.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","calculatorservice.jar"]