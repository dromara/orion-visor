#!/bin/bash
set -e

# 版本号
version=2.4.1
# 是否推送镜像
push_images=false
# 是否本地构建
locally_build=false
# 是否预构建 service
pre_build_service=false
# 是否预构建 ui
pre_build_ui=false
# 备份后缀
backup_suffix=".bak"
# 镜像命名空间
namespace="registry.cn-hangzhou.aliyuncs.com/orionsec"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
  case "$1" in
    -p|--push)
      push_images=true
      shift
      ;;
    -l|--locally)
      locally_build=true
      shift
      ;;
    -pre-service|--prebuild-service)
      pre_build_service=true
      shift
      ;;
    -pre-ui|--prebuild-ui)
      pre_build_ui=true
      shift
      ;;
    *)
      echo "未知参数: $1"
      exit 1
      ;;
  esac
done

# 要处理的 Dockerfile 列表及对应的镜像名称
declare -A images=(
  ["./ui/Dockerfile"]="orion-visor-ui"
  ["./service/Dockerfile"]="orion-visor-service"
  ["./mysql/Dockerfile"]="orion-visor-mysql"
  ["./redis/Dockerfile"]="orion-visor-redis"
  ["./adminer/Dockerfile"]="orion-visor-adminer"
  ["./guacd/Dockerfile"]="orion-visor-guacd"
)

# 准备 service app.jar
function prepare_app_jar() {
  local source_file="../orion-visor-launch/target/orion-visor-launch.jar"
  local target_file="./service/app.jar"
  if [ ! -f "$target_file" ]; then
    echo "警告: $target_file 不存在, 正在尝试从 $source_file 复制..."
    if [ -f "$source_file" ]; then
      cp "$source_file" "$target_file"
      echo "✅ 已成功复制 $source_file 至 $target_file"
    else
      echo "❌ 错误: $source_file 不存在, 无法继续构建."
      exit 1
    fi
  else
    echo "✅ $target_file 已存在, 无需复制."
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
      echo "✅ 已成功复制 $source_dir 至 $target_dir"
    else
      echo "❌ 错误: $source_dir 不存在, 无法继续构建."
      exit 1
    fi
  else
    echo "✅ $target_dir 已存在, 无需复制."
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
      echo "✅ 已成功复制 ../sql 至 $target_dir"
    else
      echo "❌ 错误: $source_dir 不存在！根据预期它应该存在, 请确认路径或项目结构是否正确。"
      exit 1
    fi
  else
    echo "✅ $target_dir 已存在, 无需复制."
  fi
}

# 修改 Dockerfile 前的备份
function modify_dockerfiles() {
  if [ "$locally_build" = true ]; then
    echo "跳过 Dockerfile 修改"
    return
  fi
  for file in "${!images[@]}"; do
    if [ -f "$file" ]; then
      echo "备份并修改: $file"
      cp "$file" "$file$backup_suffix"
      sed -i 's/--platform=\$BUILDPLATFORM//g' "$file"
    else
      echo "文件不存在 -> $file"
    fi
  done
}

# 恢复原始 Dockerfile
function restore_dockerfiles() {
  if [ "$locally_build" = true ]; then
    return
  fi
  for file in "${!images[@]}"; do
    if [ -f "$file$backup_suffix" ]; then
      echo "恢复: $file"
      rm -rf "$file"
      mv "$file$backup_suffix" "$file"
    fi
  done
}

# 构建镜像
function build_images() {
  for dockerfile in "${!images[@]}"; do
    image_name="${images[$dockerfile]}"
    echo "Building $image_name with version $version and latest tag."
    docker build -f "$dockerfile" \
      -t "${image_name}:${version}" \
      -t "${image_name}:latest" \
      -t "${namespace}/${image_name}:${version}" \
      -t "${namespace}/${image_name}:latest" .
  done
}

# 推送镜像
function push_images_to_registry() {
  if [ "$push_images" = true ]; then
    echo "📤 正在推送镜像..."
    for image_name in "${images[@]}"; do
      docker push "${namespace}/${image_name}:${version}"
      docker push "${namespace}/${image_name}:latest"
    done
  fi
}

# 执行预构建 service
function run_prebuild_service() {
  if [ "$pre_build_service" = false ]; then
    return
  fi
  echo "🛠️ 开始执行 service 预构建流程..."

  local builder_dockerfile="./service/Builder.Dockerfile"
  local builder_image="orion-visor-service-builder"
  local builder_container="orion-visor-service-prebuilder"
  local target_dir="../orion-visor-launch/target"
  local target_jar="$target_dir/orion-visor-launch.jar"

  # 确保目标目录存在
  if [ ! -d "$target_dir" ]; then
    echo "📁 创建目标目录: $target_dir"
    mkdir -p "$target_dir"
  else
    # 如果 jar 已存在, 先删除
    if [ -f "$target_jar" ]; then
      echo "🗑️ 删除已有文件: $target_jar"
      rm -f "$target_jar"
    fi
  fi

  # 清理旧容器
  local container_id=$(docker ps -a -f "name=$builder_container" --format "{{.ID}}")
  if [ -n "$container_id" ]; then
    echo "🧹 删除旧容器: $builder_container"
    docker rm -f "$container_id"
  fi

  # 构建预构建镜像
  echo "🏗️ 正在构建预构建镜像..."
  docker build \
    -f "$builder_dockerfile" \
    -t "$builder_image" .

  # 创建一个临时容器用于拷贝文件
  echo "📁 创建临时容器以提取 jar 文件..."
  docker create --name "$builder_container" "$builder_image" > /dev/null

  # 拷贝构建好的 jar 文件到目标路径
  echo "📂 正在从容器中拷贝 jar 文件..."
  docker cp "$builder_container":/build/target/orion-visor-launch.jar "$target_jar"

  # 清理临时容器
  docker rm -f "$builder_container" > /dev/null
  echo "✅ 后端预构建完成, jar 文件已保存至: $target_jar"
}

# 执行预构建 ui
function run_prebuild_ui() {
  if [ "$pre_build_ui" = false ]; then
    return
  fi
  echo "🛠️ 开始执行 ui 预构建流程..."

  local builder_dockerfile="./ui/Builder.Dockerfile"
  local builder_image="orion-visor-ui-builder"
  local builder_container="orion-visor-ui-prebuilder"
  local target_dir="../orion-visor-ui/dist"

  # 如果 dist 已存在, 先删除
  if [ -d "$target_dir" ]; then
    echo "🗑️ 删除已有目录: $target_dir"
    rm -rf "$target_dir"
  fi

  # 清理旧容器
  local container_id=$(docker ps -a -f "name=$builder_container" --format "{{.ID}}")
  if [ -n "$container_id" ]; then
    echo "🧹 删除旧容器: $builder_container"
    docker rm -f "$container_id"
  fi

  # 构建前端镜像
  echo "🏗️ 正在构建前端预构建镜像..."
  docker build \
    -f "$builder_dockerfile" \
    -t "$builder_image" .

  # 创建临时容器用于拷贝文件
  echo "📁 创建临时容器以提取 dist 文件..."
  docker create --name "$builder_container" "$builder_image" > /dev/null

  # 拷贝 dist 目录
  echo "📂 正在从容器中拷贝 dist 文件..."
  docker cp "$builder_container":/app/dist "$target_dir"

  # 清理临时容器
  docker rm "$builder_container" > /dev/null

  echo "✅ 前端预构建完成, dist 已保存至: $target_dir"
}

# 预构建
run_prebuild_service
run_prebuild_ui

# 检查资源
echo "🔍 正在检查并准备必要的构建资源..."
prepare_app_jar
prepare_dist_directory
prepare_sql_directory
echo "✅ 所有前置资源已准备完毕。"

# 修改镜像文件
echo "✅ 正在备份并修改 Dockerfile..."
modify_dockerfiles

# 设置异常捕获, 确保失败时恢复 Dockerfile
trap 'restore_dockerfiles; echo "❌ 构建失败, 已恢复原始 Dockerfile"; exit 1' ERR INT

# 构建镜像
echo "🏗️ 正在构建镜像..."
build_images

# 推送镜像
echo "📦 正在推送镜像..."
push_images_to_registry

# 恢复原始 Dockerfile
restore_dockerfiles
trap - ERR INT
echo "✅ 构建完成, Dockerfile 已恢复为原始版本"