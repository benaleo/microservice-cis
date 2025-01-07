FROM docker.elastic.co/logstash/logstash:8.15.3

# Install nano
# RUN apt-get update && apt-get install -y nano && rm -rf /var/lib/apt/lists/*

# Copy the PostgreSQL JDBC driver to the external library folder of Logstash
COPY bin/ /usr/share/logstash/logstash-core/lib/jars/

# Copy configuration files
COPY config/ /usr/share/logstash/config/

# Run Logstash with the specified config file
CMD ["logstash", "-f", "/usr/share/logstash/config/postsync.conf"]
