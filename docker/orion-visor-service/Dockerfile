FROM nginx:alpine
USER root
WORKDIR /app
# 系统时区
ARG TZ=Asia/Shanghai
# 添加包
RUN \
  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk update && \
  apk add tzdata && \
  apk add openjdk8
# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone
# 删除原 nginx 配置
RUN rm -rf /etc/nginx/conf.d/*
# 复制包
COPY ./orion-visor-launch.jar /app/app.jar
COPY ./dist /usr/share/nginx/html
COPY ./entrypoint.sh /app/entrypoint.sh
COPY ./nginx.conf /etc/nginx/conf.d
# 启动
ENTRYPOINT [ "sh", "/app/entrypoint.sh" ]
