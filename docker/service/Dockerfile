FROM --platform=$BUILDPLATFORM openjdk:8-jdk-alpine
USER root
WORKDIR /app
# 系统时区
ARG TZ=Asia/Shanghai
# 添加包
RUN \
  sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
  apk update && \
  apk add curl && \
  apk add udev && \
  apk add tzdata && \
  apk add dmidecode
# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone
# 复制包
COPY ./orion-visor-launch.jar /app/app.jar
# 启动
CMD ["java", "-jar", "/app/app.jar"]
