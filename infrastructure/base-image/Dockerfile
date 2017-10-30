FROM openjdk:8
MAINTAINER Michael Mittermayr

# Install Filebeat
ENV FILEBEAT_VERSION=5.6.3

RUN set -x && \
  apt-get update && \
  apt-get install -y wget && \
  wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-${FILEBEAT_VERSION}-linux-x86_64.tar.gz -O /opt/filebeat.tar.gz && \
  cd /opt && \
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
  apt-get install -y nano net-tools curl htop dnsutils && \
  rm -rf /var/lib/apt/lists/*

# app directory
RUN mkdir -p /app/storage

COPY docker-entrypoint.sh /
COPY run_app.sh /usr/share/
RUN chmod +x docker-entrypoint.sh && mkdir -p /beat && chmod +x /usr/share/run_app.sh

EXPOSE 50050
ENTRYPOINT ["/docker-entrypoint.sh"]
WORKDIR /

COPY ./config/filebeat.yml /beat/
COPY ./config/filebeat.template* /beat/
