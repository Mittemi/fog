FROM deb.hw.sintrum.at:5000/fogbaseimage
MAINTAINER Michael Mittermayr

ENV SERVER_PORT 8081
ENV EUREKA_SERVICE_URL http://192.168.2.100:8761
ENV EUREKA_CLIENT_IP 192.168.2.100 

ADD target/application-housing-server.jar /usr/share/app.jar
