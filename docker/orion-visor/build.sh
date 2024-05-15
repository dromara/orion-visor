#/bin/bash
version=1.1.0
mv ../../orion-visor-launch/target/orion-visor-launch.jar ./orion-visor-launch.jar
mv ../../orion-visor-ui/dist ./dist
docker build -t orion-visor:${version} .
rm -f ./orion-visor-launch.jar
rm -rf ./dist
docker tag orion-visor:${version} registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor:${version}
docker push registry.cn-hangzhou.aliyuncs.com/lijiahangmax/orion-visor:${version}
