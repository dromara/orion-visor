FROM nginx:alpine
USER root
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
# 删除原 nginx 配置
RUN rm -rf /etc/nginx/conf.d/*
# 复制包
COPY ./dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d
# 启动
CMD ["nginx", "-g", "daemon off;"]
