#!/bin/bash
# 等待 mysql 服务
/app/wait-for-it.sh orion-ops-pro-db:3306 --timeout=60 --strict=tcp
# 等待 redis 服务
/app/wait-for-it.sh orion-ops-pro-redis:6379 --timeout=60 --strict=tcp
cd /app
nohup java -jar app.jar --spring.profiles.active=prod 2>&1 &
nginx -g 'daemon off;'