#!/bin/bash
set -e

# DockerContext: orion-visor

# 版本号
version=2.5.4
# 是否构建 service
export build_service=false
# 是否构建 ui
export build_ui=false

# 解析命令行参数
for arg in "$@"; do
  case "$arg" in
    -service|--build-service)
      export build_service=true
      ;;
    -ui|--build-ui)
      export build_ui=true
      ;;
  esac
done

# 执行构建 service
function run_build_service() {
  echo "开始执行 service 构建流程..."

  local builder_dockerfile="./builder/Dockerfile.service"
  local builder_image="orion-visor-service-builder"
  local builder_container="orion-visor-service-builder-ctn"
  local builder_output="/build/orion-visor-launch/target/orion-visor-launch.jar"
  local target_dir="../orion-visor-launch/target"
  local target_jar="$target_dir/orion-visor-launch.jar"

  # 确保目标目录存在
  if [ ! -d "$target_dir" ]; then
    echo "创建目标目录: $target_dir"
    mkdir -p "$target_dir"
  else
    # 如果 jar 已存在, 先删除
    if [ -f "$target_jar" ]; then
      echo "删除已有文件: $target_jar"
      rm -f "$target_jar"
    fi
  fi

  # 清理旧容器
  local container_id=$(docker ps -a -f "name=$builder_container" --format "{{.ID}}")
  if [ -n "$container_id" ]; then
    echo "删除旧容器: $builder_container"
    docker rm -f "$container_id"
  fi

  # 构建构建镜像
  echo "正在构建 service builder image..."
  docker build \
    -f "$builder_dockerfile" \
    -t "$builder_image:$version" ../

  # 创建一个临时容器用于拷贝文件
  echo "创建临时容器以提取 jar 文件..."
  docker create --name "$builder_container" "$builder_image:$version" > /dev/null

  # 拷贝构建好的 jar 文件到目标路径
  echo "正在从容器中拷贝 jar 文件..."
  docker cp "$builder_container:$builder_output" "$target_jar"

  # 清理临时容器
  docker rm -f "$builder_container" > /dev/null
  echo "后端构建完成, jar 文件已保存至: $target_jar"
}

# 执行构建 ui
function run_build_ui() {
  echo "开始执行 ui 构建流程..."

  local builder_dockerfile="./builder/Dockerfile.ui"
  local builder_image="orion-visor-ui-builder"
  local builder_container="orion-visor-ui-builder-ctn"
  local builder_output="/build/dist"
  local target_dir="../orion-visor-ui/dist"

  # 如果 dist 已存在, 先删除
  if [ -d "$target_dir" ]; then
    echo "删除已有目录: $target_dir"
    rm -rf "$target_dir"
  fi

  # 清理旧容器
  local container_id=$(docker ps -a -f "name=$builder_container" --format "{{.ID}}")
  if [ -n "$container_id" ]; then
    echo "删除旧容器: $builder_container"
    docker rm -f "$container_id"
  fi

  # 构建前端镜像
  echo "正在构建 ui builder image..."
  docker build \
    -f "$builder_dockerfile" \
    -t "$builder_image:$version" ../

  # 创建临时容器用于拷贝文件
  echo "创建临时容器以提取 dist 文件..."
  docker create --name "$builder_container" "$builder_image:$version" > /dev/null

  # 拷贝 dist 目录
  echo "正在从容器中拷贝 dist 文件..."
  docker cp "$builder_container:$builder_output" "$target_dir"

  # 清理临时容器
  docker rm "$builder_container" > /dev/null

  echo "前端构建完成, dist 已保存至: $target_dir"
}

# 构建项目-service
if [ "$build_service" = true ]; then
  run_build_service
fi

# 构建项目-ui
if [ "$build_ui" = true ]; then
  run_build_ui
fi
