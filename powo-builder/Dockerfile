FROM rbgkew/gke-helm-deployer:0.1.6

ADD requirements.txt /

RUN set -ex \
    # runtime dependencies
    && apk add --update --no-cache python3 \
    # build dependencies
    && apk add --no-cache --virtual .build-deps python3-dev alpine-sdk linux-headers \
    # use latest pip
    && pip3 install --upgrade pip \
    # install requirements
    && pip3 install --no-cache-dir -r requirements.txt \
    # remove build dependencies
    && apk del .build-deps

ADD *.py /

ENV ENVIRONMENT test
ENV DEPLOY_TAG master

CMD ["python3", "builder.py"]
