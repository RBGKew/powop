FROM nginx:1.15-alpine

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

COPY run.sh .
RUN chmod 775 run.sh

CMD ["./run.sh"]
