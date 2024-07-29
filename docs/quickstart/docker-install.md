### 所需环境

* Docker 20.10.14+
* Docker Compose 2.3.3+

由于访问 DockerHub 镜像比较慢, 可以修改一下配置加速镜像 /etc/docker/daemon.json, 如果没有此文件可以创建此文件 (Linux系统, Window 和 Mac 直接可以通过 Docker 的
Dashboard 修改)

 ```json
 {
  "registry-mirrors": [
    "https://registry.docker-cn.com",
    "https://registry.cn-hangzhou.aliyuncs.com",
    "https://mirror.ccs.tencentyun.com",
    "https://docker.mirrors.ustc.edu.cn"
  ]
}
 ```

### 拉取代码

```shell
# github
git clone https://github.com/lijiahangmax/orion-visor
# gitee
git clone https://gitee.com/lijiahangmax/orion-visor
```

### 构建镜像

```
# 进入仓库目录
cd orion-visor
# 创建名为 .env 的 .env.example 副本
cp .env.example .env
# 将其中的值删除以保持默认或将其修改为你喜欢的值
    # SERVICE_PORT            你希望服务监听的端口
    # VOLUME_BASE             你希望数据持久化保存的目录, 如果不提前创建将以 docker 进程宿主身份创建(通常是 root)

    # MYSQL_HOST              mysql 服务所在的主机, 如果你没有现有的 MySQL 请保持值为 mysql, 如果你有自部署的请在 docker-compose.yml 中移除 services.mysql 以节约性能
    # MYSQL_PORT              mysql 监听的端口
    # MYSQL_DATABASE          mysql 数据库
    # MYSQL_USER              mysql 用户名
    # MYSQL_PASSWORD          mysql 用户密码
    # MYSQL_ROOT_PASSWORD     mysql root 密码

    # REDIS_HOST              redis 服务所在的主机, 如果你没有现有的 Redis 请保持值为 redis, 如果你有自部署的请在 docker-compose.yml 中移除 services.redis 以节约性能
    # REDIS_PASSWORD          redis 密码

    # SECRET_KEY              加密密钥
```

### 启动

```shell
docker compose up -d
```

### 修改加密方式

```
访问 adminer: http://localhost:8081
服务器: orion-visor-mysql
用户名: root
密  码: Data@123456
数据库: orion_visor

点击左侧 SQL命令 输入:
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'Data@123456';
执行 OK
```

### 测试访问

在浏览器中输入 http://localhost:1081/ 访问  
账号: `admin`  
密码: `admin`  
