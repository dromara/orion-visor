FROM --platform=$BUILDPLATFORM redis:6.0.16-alpine
WORKDIR /data
# 系统时区
ARG TZ=Asia/Shanghai
# 添加包
RUN \
  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk update && \
  apk add tzdata
# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone
# redis 配置
COPY ./redis.conf /tmp
RUN cat /tmp/redis.conf > /usr/local/redis.conf
