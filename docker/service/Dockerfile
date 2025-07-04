# 第一阶段：Maven构建阶段
FROM --platform=$BUILDPLATFORM maven:3.9.10-eclipse-temurin-8-alpine AS builder

# 设置阿里云镜像加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 复制POM文件先进行依赖下载（利用Docker缓存）
WORKDIR /build
COPY . .
RUN mvn dependency:go-offline

# 构建
RUN mvn clean package -DskipTests

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

# 从构建阶段复制jar包
COPY --from=builder /build/orion-visor-launch/target/orion-visor-launch.jar /app/app.jar

# 启动
CMD ["java", "-jar", "/app/app.jar"]