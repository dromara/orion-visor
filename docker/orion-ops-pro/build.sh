#/bin/bash
version=1.0.7
cp ../../sql/* ./
mv ../../orion-ops-ui/dist ./dist
docker build -t orion-ops-pro:${version} .
rm -f ./orion-ops-launch.jar
rm -rf ./dist
docker tag orion-ops-pro:${version} registry.cn-hangzhou.aliyuncs.com/orion-ops/orion-ops-pro:${version}
docker push registry.cn-hangzhou.aliyuncs.com/orion-ops/orion-ops-pro:${version}
