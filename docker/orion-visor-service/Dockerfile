FROM nginx:alpine
USER root
RUN \
  echo "" > /etc/apk/repositories && \
  echo "http://mirrors.aliyun.com/alpine/v3.8/main" >> /etc/apk/repositories && \
  echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories && \
  apk update
RUN apk add openjdk8
RUN rm -rf /etc/nginx/conf.d/*
WORKDIR /app
COPY ./orion-visor-launch.jar /app/app.jar
COPY ./dist /usr/share/nginx/html
COPY ./entrypoint.sh /app/entrypoint.sh
COPY ./nginx.conf /etc/nginx/conf.d
ENTRYPOINT [ "sh", "/app/entrypoint.sh" ]
