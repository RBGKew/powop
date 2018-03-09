FROM httpd:2.4-alpine

COPY conf/tomcat.conf conf.d/
COPY conf/httpd.conf conf/
COPY src/google4faeb6962aa9d560.html htdocs/
COPY target/static/img/ htdocs/img
COPY target/static/fonts/ htdocs/fonts
COPY target/static/css/ htdocs/css
COPY target/static/js/ htdocs/js
COPY apache.sh ./
RUN chmod +x ./apache.sh
CMD [ "./apache.sh" ]
