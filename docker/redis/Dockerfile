FROM --platform=$BUILDPLATFORM redis:6.0.16-alpine

WORKDIR /data

# 系统时区
ARG TZ=Asia/Shanghai

# 添加包 & 设置时区
RUN \
  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk update && \
  apk add --no-cache tzdata && \
  ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo "${TZ}" > /etc/timezone && \
  rm -rf /var/cache/apk/* && \
  rm -f /usr/local/redis.conf

# 复制配置文件
COPY ./redis/redis.conf /usr/local/redis.conf

# 启动 Redis 并加载自定义配置
CMD ["redis-server", "/usr/local/redis.conf"]