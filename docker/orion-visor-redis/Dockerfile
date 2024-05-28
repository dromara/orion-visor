FROM redis:6.0.16-alpine
WORKDIR /data
# 系统时区
ARG TZ=Asia/Shanghai
# 添加包
RUN \
  echo "" > /etc/apk/repositories && \
  echo "https://mirrors.aliyun.com/alpine/v3.8/main" >> /etc/apk/repositories && \
  echo "https://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories && \
  echo "https://mirrors.tuna.tsinghua.edu.cn" >> /etc/apk/repositories && \
  apk update && \
  apk add tzdata
# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone
# redis 配置
COPY ./redis.conf /tmp
RUN cat /tmp/redis.conf > /usr/local/redis.conf
