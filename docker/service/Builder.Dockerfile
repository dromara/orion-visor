FROM maven:3.9.10-eclipse-temurin-8-alpine AS builder

# 设置阿里云镜像加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 拷贝 settings.xml
COPY ./service/settings.xml /root/.m2/

# 复制 POM 文件先进行依赖下载 (利用 Docker 缓存)
WORKDIR /build
COPY ../pom.xml .
RUN mvn dependency:resolve

# 构建
COPY ../ .
RUN mvn clean package -DskipTests
