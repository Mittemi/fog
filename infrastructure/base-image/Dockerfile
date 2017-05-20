FROM openjdk:8
MAINTAINER Michael Mittermayr

# Install Filebeat
ENV FILEBEAT_VERSION=5.4.0 \
    FILEBEAT_SHA1=545fbb229c958f2379b17efe3825bf0c30e3039b

RUN set -x && \
  apt-get update && \
  apt-get install -y wget && \
  wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz -O /opt/filebeat.tar.gz && \
  cd /opt && \
  echo "${FILEBEAT_SHA1}  filebeat.tar.gz" | sha1sum -c - && \
  tar xzvf filebeat.tar.gz && \
  cd filebeat-* && \
  cp filebeat /bin && \
  cd /opt && \
  rm -rf filebeat* && \
  apt-get purge -y wget && \
  apt-get autoremove -y && \
  apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Install Supervisor.
RUN \
  apt-get update && \
  apt-get install -y supervisor && \
  rm -rf /var/lib/apt/lists/* && \
  sed -i 's/^\(\[supervisord\]\)$/\1\nnodaemon=true/' /etc/supervisor/supervisord.conf

# Define mountable directories.
VOLUME ["/etc/supervisor/conf.d"]

# Create application logs directory
RUN mkdir -p /logs
VOLUME ["/logs"]

# Install additional tools, remove for production!
RUN \
  apt-get update && \
  apt-get install -y nano net-tools curl && \
  rm -rf /var/lib/apt/lists/*


COPY docker-entrypoint.sh /
RUN chmod +x docker-entrypoint.sh && mkdir -p /beat && \
    echo "java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=50050 -Dspring.profiles.active=docker -jar /usr/share/app.jar" > /usr/share/run_app.sh && \
    chmod +x /usr/share/run_app.sh
EXPOSE 50050
ENTRYPOINT ["/docker-entrypoint.sh"]
WORKDIR /

COPY ./config/filebeat.yml /beat/
COPY ./config/filebeat.template* /beat/
