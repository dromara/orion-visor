#/bin/bash
version=2.3.5
mv ../../orion-visor-launch/target/orion-visor-launch.jar ./orion-visor-launch.jar
mv ../../orion-visor-ui/dist ./dist
docker build -t orion-visor-service:${version} .
rm -rf ./orion-visor-launch.jar
rm -rf ./dist
docker tag orion-visor-service:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:${version}
docker tag orion-visor-service:${version} registry.cn-hangzhou.aliyuncs.com/orionsec/orion-visor-service:latest
