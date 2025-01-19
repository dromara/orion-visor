#/bin/bash
version=2.3.0
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:latest
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:latest
