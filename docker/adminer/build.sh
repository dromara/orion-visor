#/bin/bash
version=2.2.1
docker build -t orion-visor-adminer:${version} .
docker tag orion-visor-adminer:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-adminer:${version}
