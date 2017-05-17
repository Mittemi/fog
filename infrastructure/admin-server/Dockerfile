FROM openjdk:8
MAINTAINER Michael Mittermayr

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]

ENV SERVER_PORT 8080
ENV EUREKA_SERVICE_URL=http://eureka:8761

EXPOSE $SERVER_PORT

ADD target/admin-server.jar /usr/share/app.jar
