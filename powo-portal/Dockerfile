FROM tomcat:9-jre11-slim

COPY docker/bin/ bin/
COPY docker/conf/ conf/

RUN chmod 775 bin/setenv.sh
RUN rm -rf webapps/*

ADD target/powo-portal.war webapps/ROOT.war
