FROM --platform=$TARGETPLATFORM nginx:1.31.10

# 系统时区
ARG TZ=Asia/Shanghai

# 设置时区 & 清理默认配置
RUN \
  apt-get update && \
  DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends tzdata && \
  ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo "${TZ}" > /etc/timezone && \
  rm -rf /var/lib/apt/lists/* && \
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