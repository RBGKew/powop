FROM nginx:1.15-alpine

COPY run.sh /
RUN chmod 775 /run.sh

ADD default.conf /etc/nginx/conf.d/default.conf
ADD nginx.conf /etc/nginx/nginx.conf

WORKDIR /www/data

COPY src/google4faeb6962aa9d560.html .
COPY target/static/img/ img
COPY target/static/fonts/ fonts
COPY target/static/css/ css
COPY target/static/js/ js

RUN  mkdir admin
COPY target/admin/index.html admin/index.html
COPY target/admin/css/ admin/css
COPY target/admin/js/ admin/js

# update permissions so that Nginx worker can serve static files
RUN chmod -R 755 /www/data

CMD ["/run.sh"]
