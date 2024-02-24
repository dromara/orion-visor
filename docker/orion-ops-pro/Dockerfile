FROM nginx:alpine
USER root
RUN apk add openjdk8
RUN rm -rf /etc/nginx/conf.d/*
WORKDIR /app
COPY orion-ops-launch/target/orion-ops-launch.jar /app/app.jar
COPY orion-ops-ui/dist /usr/share/nginx/html
ADD docker/orion-ops-pro/entrypoint.sh /app/entrypoint.sh
ADD docker/orion-ops-pro/nginx.conf /etc/nginx/conf.d
ENTRYPOINT [ "sh", "/app/entrypoint.sh" ]
