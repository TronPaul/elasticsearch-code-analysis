FROM elasticsearch:2

COPY target/releases/code-analysis-2.3.5.zip /tmp/code-analysis-2.3.5.zip

RUN /usr/share/elasticsearch/bin/plugin install file:/tmp/code-analysis-2.3.5.zip && \
    rm /tmp/code-analysis-2.3.5.zip
