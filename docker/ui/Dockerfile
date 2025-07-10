FROM --platform=$TARGETPLATFORM nginx:alpine

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
  rm -rf /etc/nginx/conf.d/*

# 复制包
COPY ./ui/dist /usr/share/nginx/html

# 复制配置
COPY ./ui/nginx.conf /etc/nginx/conf.d

# 启动
CMD ["nginx", "-g", "daemon off;"]