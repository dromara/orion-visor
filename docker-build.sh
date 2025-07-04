#!/bin/bash
set -e

# 版本
version=2.4.1
# 是否推送
push_images=false
# 命令空间
namespace="registry.cn-hangzhou.aliyuncs.com/orionsec"

# 解析参数
while [[ $# -gt 0 ]]; do
    case "$1" in
        --push)
            push_images=true
            shift
            ;;
        *)
            echo "未知参数: $1"
            exit 1
            ;;
    esac
done

docker build -f ./docker/ui/Dockerfile -t orion-visor-ui:${version} -t ${namespace}/orion-visor-ui:${version} -t ${namespace}/orion-visor-ui:${version} . && \
docker build -f ./docker/service/Dockerfile -t orion-visor-service:${version} -t ${namespace}/orion-visor-service:${version} -t ${namespace}/orion-visor-service:${version} . && \
docker build -f ./docker/mysql/Dockerfile -t orion-visor-mysql:${version} -t ${namespace}/orion-visor-mysql:${version} -t ${namespace}/orion-visor-mysql:${version} . && \
docker build -f ./docker/redis/Dockerfile -t orion-visor-redis:${version} -t ${namespace}/orion-visor-redis:${version} -t ${namespace}/orion-visor-redis:${version} . && \
docker build -f ./docker/adminer/Dockerfile -t orion-visor-adminer:${version} -t ${namespace}/orion-visor-adminer:${version} -t ${namespace}/orion-visor-adminer:${version} . && \
docker build -f ./docker/guacd/Dockerfile -t orion-visor-guacd:${version} -t ${namespace}/orion-visor-guacd:${version} -t ${namespace}/orion-visor-guacd:${version} .

# 推送镜像
if [ "$push_images" = true ]; then
    docker push ${namespace}/orion-visor-adminer:${version}
    docker push ${namespace}/orion-visor-mysql:${version}
    docker push ${namespace}/orion-visor-redis:${version}
    docker push ${namespace}/orion-visor-guacd:${version}
    docker push ${namespace}/orion-visor-service:${version}
    docker push ${namespace}/orion-visor-ui:${version}
fi
