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
  rm -rf /etc/nginx/nginx.conf && \
  rm -rf /etc/nginx/conf.d/*

# 复制前端静态文件
COPY ./ui/dist /usr/share/nginx/html

# 复制配置
COPY ./ui/nginx.conf /etc/nginx
COPY ./ui/service.conf /etc/nginx/conf.d

# 复制启动脚本
COPY ./ui/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# 启动
ENTRYPOINT ["/entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]