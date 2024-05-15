#/bin/bash
version=1.1.0
mv ../../orion-ops-launch/target/orion-ops-launch.jar ./orion-ops-launch.jar
mv ../../orion-ops-ui/dist ./dist
docker build -t orion-ops-pro:${version} .
rm -f ./orion-ops-launch.jar
rm -rf ./dist
docker tag orion-ops-pro:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-ops-pro:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-ops-pro:${version}
