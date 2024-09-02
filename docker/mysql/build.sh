#/bin/bash
version=2.1.5
cp -r ../../sql ./sql
docker build -t orion-visor-mysql:${version} .
rm -rf ./sql
docker tag orion-visor-mysql:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor-mysql:${version}
