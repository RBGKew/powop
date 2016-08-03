FROM solr:5.5.2

ENV CORE_DIR /opt/solr/server/solr/powop
ENV DATA_DIR /opt/solr/server/solr/powop/data
ENV LIB_DIR /opt/solr/server/lib

COPY powop $CORE_DIR
COPY lib $LIB_DIR

USER root
RUN mkdir $DATA_DIR && chown -R $SOLR_USER:$SOLR_USER $CORE_DIR $LIB_DIR
USER $SOLR_USER

VOLUME $DATA_DIR
