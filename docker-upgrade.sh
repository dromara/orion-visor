#!/bin/bash

# 初始化标志变量
PULL_IMAGES=false
DEMO_MODE=false

# 解析命令行参数
for arg in "$@"
do
    case $arg in
        --pull)
        PULL_IMAGES=true
        shift
        ;;
        --demo)
        DEMO_MODE=true
        shift
        ;;
        *)
        echo "Unknown argument: $arg"
        exit 1
        ;;
    esac
done

# 停止并移除现有容器
echo "Stopping all services..."
docker compose down --remove-orphans
echo "Stopped all services..."

# 拉取镜像
if [ "$PULL_IMAGES" = true ]; then
    echo "Pulling latest images..."
    docker compose pull
    echo "Pulled latest images..."
fi

if [ "$DEMO_MODE" = true ]; then
    # 启用 demo 模式
    export DEMO_MODE=true
    echo "Starting services for demo mode..."

    # 启动指定的服务
    docker compose up -d --remove-orphans mysql redis ui service guacd adminer
    echo "Started services for demo mode..."
else
    # 启动所有服务
    echo "Starting all services..."
    docker compose up -d --remove-orphans
    echo "Started all services..."
fi
