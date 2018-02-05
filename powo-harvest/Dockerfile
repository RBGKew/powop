FROM tomcat:8.0-jre8-alpine

COPY docker/bin/ bin/
COPY docker/conf/ conf/

RUN rm -rf webapps/* \
  && mkdir work/powop

ADD target/powo-harvest.war webapps/harvester.war
