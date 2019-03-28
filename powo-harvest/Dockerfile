FROM tomcat:9-jre11

COPY docker/bin/ bin/
COPY docker/conf/ conf/

RUN rm -rf webapps/* \
  && mkdir work/powop

ADD target/powo-harvest.war webapps/harvester.war
