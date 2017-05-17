FROM openjdk:8
MAINTAINER Michael Mittermayr

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]

EXPOSE 8761

ADD target/eureka-server.jar /usr/share/app.jar