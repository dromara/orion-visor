#/bin/bash
set -e
version=1.0.0
docker build -t orion-visor-guacd:${version} .
docker tag orion-visor-guacd:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-guacd:${version}
docker tag orion-visor-guacd:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-guacd:latest
