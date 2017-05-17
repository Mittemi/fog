FROM openjdk:8
MAINTAINER Michael Mittermayr

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]

EXPOSE 8080

ENV EUREKA_SERVICE_URL=http://eureka:8761

ADD target/admin-server.jar /usr/share/app.jar