#/bin/bash
version=2.0.11
cp -r ../../sql ./sql
docker build -t orion-visor-mysql:${version} .
rm -rf ./sql
docker tag orion-visor-mysql:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-mysql:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-mysql:${version}
