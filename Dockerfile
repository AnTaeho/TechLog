FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar tech-log-server.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/tech-log-server.jar"]
