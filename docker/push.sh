#/bin/bash
version=2.2.1
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-adminer:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-mysql:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-redis:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version}
