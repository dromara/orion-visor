FROM --platform=$TARGETPLATFORM openjdk:8-jdk-alpine

USER root

WORKDIR /app

# 系统时区
ARG TZ=Asia/Shanghai

# 添加包 & 设置时区
RUN \
  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk update && \
  apk add curl && \
  apk add udev && \
  apk add tzdata && \
  ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo "${TZ}" > /etc/timezone

# 复制启动脚本
COPY ./service/entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# 复制 jar 包
COPY ./service/orion-visor-launch.jar /app/app.jar
# 复制探针包
ADD ./service/instance-agent-release.tar.gz /app/instance-agent-release

# 启动检测
HEALTHCHECK --interval=15s --timeout=5s --retries=5 --start-period=10s \
  CMD wget -T5 -qO- http://127.0.0.1:9200/orion-visor/api/server/bootstrap/health | grep ok || exit 1

# 启动
ENTRYPOINT ["/app/entrypoint.sh"]
CMD ["java", "-jar", "/app/app.jar"]
