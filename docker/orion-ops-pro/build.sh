mv ../../orion-ops-launch/target/orion-ops-launch.jar ./
mv ../../orion-ops-ui/dist ./dist
docker build -t orion-ops-pro:1.0.0 .
