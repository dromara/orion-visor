FROM --platform=$BUILDPLATFORM guacamole/guacd:1.6.0

USER root

# 系统时区
ARG TZ=Asia/Shanghai

# 添加包 & 设置时区
RUN \
  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk update && \
  apk add --no-cache tzdata && \
  ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo "${TZ}" > /etc/timezone

# 创建所需目录
RUN mkdir -p /home/guacd/drive /usr/share/guacd/drive
