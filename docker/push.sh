#/bin/bash
version=2.2.1
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-adminer:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-mysql:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-redis:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-service:${version}
