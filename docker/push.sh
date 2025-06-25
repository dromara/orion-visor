#/bin/bash
set -e
version=2.3.9
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-guacd:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-ui:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-guacd:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-ui:latest
