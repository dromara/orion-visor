FROM nginx:alpine
USER root
WORKDIR /app
# 系统时区
ARG TZ=Asia/Shanghai
# 切换源
RUN \
  echo "" > /etc/apk/repositories && \
  echo "http://mirrors.aliyun.com/alpine/v3.8/main" >> /etc/apk/repositories && \
  echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories && \
  apk update
# 设置时区
RUN apk add tzdata && \
  ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone
# 添加 jdk
RUN apk add openjdk8
# 删除原 nginx 配置
RUN rm -rf /etc/nginx/conf.d/*
# 复制包
COPY ./orion-visor-launch.jar /app/app.jar
COPY ./dist /usr/share/nginx/html
COPY ./entrypoint.sh /app/entrypoint.sh
COPY ./nginx.conf /etc/nginx/conf.d
# 启动
ENTRYPOINT [ "sh", "/app/entrypoint.sh" ]
