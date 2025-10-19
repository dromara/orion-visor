#!/bin/bash
set -e

# DockerContext: orion-visor/docker

# 加载项目构建
source ./project-build.sh "$@"

# 版本号
version=2.5.4
# 是否推送镜像
push_image=false
# 是否构建 latest
latest_image=false
# 是否本地构建
locally_build=false
# 备份后缀
backup_suffix=".bak"
# 镜像命名空间
namespace="registry.cn-hangzhou.aliyuncs.com/orionsec"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
  case "$1" in
    -l|--locally)
      locally_build=true
      shift
      ;;
    -latest|--latest-image)
      latest_image=true
      shift
      ;;
    -push|--push-image)
      push_image=true
      shift
      ;;
    *)
      shift
      ;;
  esac
done

# 要处理的 Dockerfile 列表及对应的镜像名称
declare -A images=(
  ["./ui/Dockerfile"]="orion-visor-ui"
  ["./service/Dockerfile"]="orion-visor-service"
  ["./mysql/Dockerfile"]="orion-visor-mysql"
  ["./redis/Dockerfile"]="orion-visor-redis"
  ["./influxdb/Dockerfile"]="orion-visor-influxdb"
  ["./adminer/Dockerfile"]="orion-visor-adminer"
  ["./guacd/Dockerfile"]="orion-visor-guacd"
)

# 准备 service jar
function prepare_app_jar() {
  local source_file="../orion-visor-launch/target/orion-visor-launch.jar"
  local target_file="./service/orion-visor-launch.jar"
  if [ ! -f "$target_file" ]; then
    echo "警告: $target_file 不存在, 正在尝试从 $source_file 复制..."
    if [ -f "$source_file" ]; then
      cp "$source_file" "$target_file"
      echo "已成功复制 $source_file 至 $target_file"
    else
      echo "错误: $source_file 不存在, 无法继续构建."
      exit 1
    fi
  else
    echo "$target_file 已存在, 无需复制."
  fi
}

# 准备 instance-agent
function prepare_instance_agent() {
  local target_file="./service/instance-agent-release.tar.gz"
  if [ ! -f "$target_file" ]; then
    echo "警告: $target_file 不存在, 正在尝试从 Github Release 下载..."
    # 尝试从 GitHub Release 下载
    if curl -L --fail \
         --connect-timeout 30 --max-time 30 \
         https://github.com/lijiahangmax/orion-visor-agent/releases/latest/download/instance-agent-release.tar.gz \
         -o "$target_file"; then
      echo "已成功下载到 $target_file"
    fi

    # 如果下载失败, 提示用户手动下载
    echo "错误: 无法从 Release 获取 instance-agent-release.tar.gz"
    echo "请手动从以下地址下载, 并放置到 $target_file"
    echo "   1) https://github.com/lijiahangmax/orion-visor-agent/raw/main/instance-agent-release.tar.gz"
    echo "   2) https://gitee.com/lijiahangmax/orion-visor-agent/raw/main/instance-agent-release.tar.gz"
    exit 1
  else
    echo "$target_file 已存在, 无需下载."
  fi
}

# 准备前端 dist 目录
function prepare_dist_directory() {
  local source_dir="../orion-visor-ui/dist"
  local target_dir="./ui/dist"
  if [ ! -d "$target_dir" ]; then
    echo "警告: $target_dir 不存在, 正在尝试从 $source_dir 复制..."
    if [ -d "$source_dir" ]; then
      cp -r "$source_dir" "$target_dir"
      echo "已成功复制 $source_dir 至 $target_dir"
    else
      echo "错误: $source_dir 不存在, 无法继续构建."
      exit 1
    fi
  else
    echo "$target_dir 已存在, 无需复制."
  fi
}

# 准备 mysql sql 目录
function prepare_sql_directory() {
  local source_dir="../sql"
  local target_dir="./mysql/sql"
  if [ ! -d "$target_dir" ]; then
    echo "警告: $target_dir 不存在, 正在尝试从 $source_dir 复制..."
    if [ -d $source_dir ]; then
      cp -r $source_dir "$target_dir"
      echo "已成功复制 ../sql 至 $target_dir"
    else
      echo "错误: $source_dir 不存在！根据预期它应该存在, 请确认路径或项目结构是否正确"
      exit 1
    fi
  else
    echo "$target_dir 已存在, 无需复制."
  fi
}

# 修改 Dockerfile 前的备份
function modify_dockerfiles() {
  if [ "$locally_build" = false ]; then
    echo "跳过 Dockerfile 修改"
    return
  fi
  echo "正在备份并修改 Dockerfile..."
  for file in "${!images[@]}"; do
    if [ -f "$file" ]; then
      echo "备份并修改: $file"
      cp "$file" "$file$backup_suffix"
      sed -i 's/--platform=\TARGETPLATFORM//g' "$file"
    else
      echo "文件不存在 -> $file"
    fi
  done
}

# 恢复原始 Dockerfile
function restore_dockerfiles() {
  if [ "$locally_build" = false ]; then
    return
  fi
  echo "开始恢复 Dockerfile"
  for file in "${!images[@]}"; do
    if [ -f "$file$backup_suffix" ]; then
      echo "恢复: $file"
      rm -rf "$file"
      mv "$file$backup_suffix" "$file"
    fi
  done
  echo "Dockerfile 已恢复为原始版本"
}

# 构建镜像
function build_images() {
  echo "构建镜像开始..."
  for dockerfile in "${!images[@]}"; do
    image_name="${images[$dockerfile]}"
    echo "Building $image_name with version $version."
    # 构建 Docker 镜像
    docker build -f "$dockerfile" -t "${image_name}:${version}" -t "${namespace}/${image_name}:${version}" .
    # 添加 latest 标签
    if [ "$latest_image" = true ]; then
      echo "Tag $image_name with latest version."
      docker tag "${image_name}:${version}" "${image_name}:latest"
      docker tag "${namespace}/${image_name}:${version}" "${namespace}/${image_name}:latest"
    fi
  done
  echo "构建镜像结束..."
}

# 推送镜像
function push_image_to_registry() {
  if [ "$push_image" = true ]; then
    echo "推送镜像开始..."
    for image_name in "${images[@]}"; do
      # 推送版本
      docker push "${namespace}/${image_name}:${version}"
      # 推送 latest
      if [ "latest_image" = true ]; then
        docker push "${namespace}/${image_name}:latest"
      fi
    done
    echo "推送镜像结束..."
  fi
}

# 构建项目-service
if [ "$build_service" = true ]; then
  run_build_service
fi

# 构建项目-ui
if [ "$build_ui" = true ]; then
  run_build_ui
fi

# 检查资源
echo "正在检查并准备必要的构建资源..."
prepare_app_jar
prepare_instance_agent
prepare_dist_directory
prepare_sql_directory
echo "所有前置资源已准备完毕"

# 修改镜像文件
modify_dockerfiles

# 设置异常捕获, 确保失败时恢复 Dockerfile
trap 'restore_dockerfiles; echo "构建失败, 已恢复原始 Dockerfile"; exit 1' ERR INT

# 构建镜像
build_images

# 推送镜像
push_image_to_registry

# 恢复原始 Dockerfile
restore_dockerfiles
trap - ERR INT
echo "构建完成"