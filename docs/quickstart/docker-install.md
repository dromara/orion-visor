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

```
# github
git clone https://github.com/lijiahangmax/orion-visor
# gitee
git clone https://gitee.com/lijiahangmax/orion-visor
```

### 构建镜像

```
# 进入仓库目录
cd orion-visor
# 修改 docker-compose.yml (建议修改)
    # MYSQL_USER          mysql 用户名
    # MYSQL_PASSWORD      mysql 用户密码
    # MYSQL_ROOT_PASSWORD mysql root 密码
    # REDIS_PASSWORD      redis 密码
    # SECRET_KEY          加密密钥
# 构建
docker compose build
```

### 启动

```
docker compose up -d
```

### 连接 mysql (如果需要在 navicat 中连接)

```
访问 adminer: http://localhost:8081
服务器: orion-visor-mysql
用户名: root
密码: Data@123456
数据库: orion-visor

点击左侧 SQL命令 输入:
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'Data@123456';
执行 OK
```

### 测试访问

在浏览器中输入 http://localhost:1081/ 访问  
账号: admin  
密码: admin  
