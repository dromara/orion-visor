# 构建应用
FROM --platform=$BUILDPLATFORM maven:3.9.10-eclipse-temurin-8-alpine AS builder

# 设置阿里云镜像加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 拷贝 settings.xml
COPY ./docker/service/settings.xml /root/.m2/

# 复制 POM 文件先进行依赖下载 (利用 Docker 缓存)
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:resolve

# 构建
COPY . .
RUN mvn clean package -DskipTests

FROM --platform=$BUILDPLATFORM openjdk:8-jdk-alpine

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

# 从构建阶段复制 jar 包
COPY --from=builder /build/orion-visor-launch/target/orion-visor-launch.jar /app/app.jar

# 启动
CMD ["java", "-jar", "/app/app.jar"]
