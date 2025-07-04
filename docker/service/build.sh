#!/bin/bash
set -e
version=2.4.1
mv ../../orion-visor-launch/target/orion-visor-launch.jar ./orion-visor-launch.jar
docker build -t orion-visor-service:${version} .
rm -rf ./orion-visor-launch.jar
docker tag orion-visor-service:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version}
docker tag orion-visor-service:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:latest
