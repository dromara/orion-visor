#/bin/bash
set -e

# ./build_docker.sh --push 这样使用会编译完成后自动推送镜像到阿里云仓库
version=2.4.1
push_images=false

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

docker build -f ./docker/ui/Dockerfile -t orion-visor-ui:${version} -t registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-ui:${version} . && \
docker build -f ./docker/service/Dockerfile -t orion-visor-service:${version} -t registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version} . && \
docker build -f ./docker/mysql/Dockerfile -t orion-visor-mysql:${version} -t registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:${version} . && \
docker build -f ./docker/redis/Dockerfile -t orion-visor-redis:${version} -t registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:${version} . && \
docker build -f ./docker/adminer/Dockerfile -t orion-visor-adminer:${version} -t registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version} . && \
docker build -f ./docker/guacd/Dockerfile -t orion-visor-guacd:${version} -t registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-guacd:${version} .


# 如果需要推送镜像
if [ "$push_images" = true ]; then
    docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version}
    docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:${version}
    docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:${version}
    docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-guacd:${version}
    docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version}
    docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-ui:${version}
fi