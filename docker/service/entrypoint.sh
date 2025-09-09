#!/bin/sh

AGENT_RELEASE_DIR="/root/orion/orion-visor/agent-release"
DEFAULT_AGENT_DIR="/app/agent-release"

# 确保父目录存在
mkdir -p "$(dirname "$AGENT_RELEASE_DIR")"

# 加载探针
if [ -d "$AGENT_RELEASE_DIR" ] && [ -n "$(ls -A "$AGENT_RELEASE_DIR" 2>/dev/null)" ]; then
    echo "Using mounted agent release: $AGENT_RELEASE_DIR"
else
    echo "Using default agent release: $DEFAULT_AGENT_DIR"
    # 复制探针
    cp -rf "$DEFAULT_AGENT_DIR" "$AGENT_RELEASE_DIR"
fi

# 打印探针版本信息
if [ -f "$AGENT_RELEASE_DIR/.version" ]; then
    echo "Agent version: $(cat "$AGENT_RELEASE_DIR/.version")"
fi

exec "$@"