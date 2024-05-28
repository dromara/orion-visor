FROM redis:6.0.16-alpine
WORKDIR /data
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
# redis 配置
COPY ./redis.conf /tmp
RUN cat /tmp/redis.conf > /usr/local/redis.conf
