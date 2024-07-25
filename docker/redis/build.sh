#/bin/bash
version=2.1.0
docker build -t orion-visor-redis:${version} .
docker tag orion-visor-redis:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-redis:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-redis:${version}
