#!/bin/bash

# 停止并移除现有容器
docker compose down --remove-orphans

if [ "$1" == "demo" ]; then
    # 设置 DEMO_MODE 环境变量为 true
    export DEMO_MODE=true
    echo "Starting services for demo mode..."
    # 启动指定的服务
    docker compose up -d --remove-orphans mysql redis ui service  adminer
else
    echo "Starting all services..."
    # 正常启动所有服务
    docker compose up -d --remove-orphans
fi