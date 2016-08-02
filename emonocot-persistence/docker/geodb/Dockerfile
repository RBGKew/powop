FROM mysql:5.5

ENV LC_ALL C.UTF-8

COPY powop.cnf /etc/mysql/conf.d/
COPY data/data.sql /docker-entrypoint-initdb.d/
