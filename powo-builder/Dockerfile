FROM rbgkew/gke-helm-deployer:0.1.5

ADD requirements.txt /

RUN apk --no-cache add python3 \
  && pip3 install --upgrade pip \
  && pip3 install -r requirements.txt

ADD *.py /
ADD data*.json /

ENV ENVIRONMENT test
ENV DEPLOY_TAG master

CMD ["python3", "builder.py"]
