FROM nginx:alpine
USER root
RUN \
  echo "" > /etc/apk/repositories && \
  echo "http://mirrors.aliyun.com/alpine/v3.8/main" >> /etc/apk/repositories &&\
  echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories && \
  apk update
RUN apk add openjdk8
RUN rm -rf /etc/nginx/conf.d/*
WORKDIR /app
COPY orion-ops-launch/target/orion-ops-launch.jar /app/app.jar
COPY orion-ops-ui/dist /usr/share/nginx/html
COPY docker/orion-ops-pro/entrypoint.sh /app/entrypoint.sh
COPY docker/orion-ops-pro/nginx.conf /etc/nginx/conf.d
ENTRYPOINT [ "sh", "/app/entrypoint.sh" ]
