#/bin/bash
version=1.0.7
cp -r ../../sql ./sql
docker build -t orion-ops-pro-mysql:${version} .
rm -rf ./sql
docker tag orion-ops-pro-mysql:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-ops-pro-mysql:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-ops-pro-mysql:${version}
