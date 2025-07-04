# 构建应用
FROM --platform=$BUILDPLATFORM node:18-alpine AS builder

# 设置阿里云镜像加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 安装 pnpm
RUN corepack enable && corepack prepare pnpm@latest --activate

WORKDIR /app

# 设置 pnpm 使用指定的 registry
ARG REGISTRY_URL=https://registry.npmmirror.com
RUN pnpm config set registry $REGISTRY_URL

# 复制项目文件 (package.json等)
COPY ./orion-visor-ui/package.json ./orion-visor-ui/pnpm-lock.yaml* ./

# 安装依赖 (利用 Docker 缓存层)
RUN pnpm install --frozen-lockfile

# 复制源代码
COPY ./orion-visor-ui/ .

# 构建项目
RUN pnpm build

# 打包静态资源
FROM --platform=$BUILDPLATFORM nginx:alpine

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
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制配置
COPY ./docker/ui/nginx.conf /etc/nginx/conf.d

# 启动
CMD ["nginx", "-g", "daemon off;"]