#/bin/bash
docker compose down
# demo 启动
if [ "$1" == "demo" ]; then
    sed -i 's/\${DEMO_MODE:-false}/true/g' docker-compose.yml
fi
docker compose up -d --remove-orphans
