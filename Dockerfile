FROM elasticsearch:2

ARG VERSION

RUN /usr/share/elasticsearch/bin/plugin install https://dl.bintray.com/tronpaul/teamunpro/com/teamunpro/elasticsearch/plugin/code-analysis/${VERSION:?VERSION empty or unset}/code-analysis-$VERSION.zip
