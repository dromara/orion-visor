FROM --platform=$BUILDPLATFORM node:18-alpine AS builder

# 设置阿里云镜像加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 安装pnpm
RUN corepack enable && corepack prepare pnpm@latest --activate

WORKDIR /app

# 复制项目文件（包括package.json等）
COPY ./orion-visor-ui/package.json ./orion-visor-ui/pnpm-lock.yaml* ./

# 安装依赖（利用Docker缓存层）
RUN pnpm install --frozen-lockfile

# 复制源代码
COPY ./orion-visor-ui/ .

# 构建项目
RUN pnpm build

FROM --platform=$BUILDPLATFORM nginx:alpine
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
COPY --from=builder /app/dist /usr/share/nginx/html
COPY ./docker/ui/nginx.conf /etc/nginx/conf.d
# 启动
CMD ["nginx", "-g", "daemon off;"]
