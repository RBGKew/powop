FROM solr:7.5-alpine

ENV CORE_DIR /opt/solr/server/solr/powop
ENV DATA_DIR /opt/solr/server/solr/powop/data
ENV LIB_DIR /opt/solr/server/lib

COPY powop $CORE_DIR
COPY lib $LIB_DIR

USER root

RUN mkdir $DATA_DIR
RUN chmod -R 755 $CORE_DIR
RUN chmod -R 755 $LIB_DIR
RUN chown -R $SOLR_USER:$SOLR_USER $CORE_DIR

USER $SOLR_USER

VOLUME $DATA_DIR
