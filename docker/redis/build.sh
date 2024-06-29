#/bin/bash
version=2.0.10
docker build -t orion-visor-redis:${version} .
docker tag orion-visor-redis:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-redis:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-redis:${version}
