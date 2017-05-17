FROM openjdk:8
MAINTAINER Michael Mittermayr

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]

ENV SERVER_PORT 8761

EXPOSE $SERVER_PORT

ADD target/eureka-server.jar /usr/share/app.jar
