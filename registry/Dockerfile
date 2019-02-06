FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8888
ARG JAR_FILE=target/registry.jar
ADD ${JAR_FILE} registry.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","registry.jar"]