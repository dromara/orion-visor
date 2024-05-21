### 所需环境

* jdk 1.8
* mysql 8.0.+
* redis 6.0.+
* maven 3.5.+
* node 18.12.+
* pnpm 9.1.+
* nginx

⚡ maven 推荐使用阿里云 mirror   
⚡ npm 建议使用淘宝镜像 `npm config set registry https://registry.npmmirror.com/`  
⚡ pnpm 建议使用淘宝镜像 `pnpm config set registry https://registry.npmmirror.com/`

### 构建

1. 拉取代码

```
# github
git clone https://github.com/lijiahangmax/orion-visor
# gitee
git clone https://gitee.com/lijiahangmax/orion-visor
```

2. 初始化数据库

```
# 执行脚本
orion-visor/sql/init-1-schema-databases.sql
orion-visor/sql/init-2-schema-tables.sql
orion-visor/sql/init-3-schema-quartz.sql
orion-visor/sql/init-4-data.sql
```

3. 构建后端代码

```
# 修改配置文件 (mysql, redis, secret-key)
orion-visor/orion-visor-launch/src/main/resources/application-prod.yaml

# 进入代码目录
cd orion-visor
# 编译
mvn -U clean install -DskipTests
```   

4. 构建前端代码

```
# 进入代码目录
cd orion-visor/orion-visor-ui
# 下载 pnpm
npm i -g pnpm
# 下载依赖
pnpm i
# 编译
pnpm build
```   

### 修改 nginx 配置

```
server {
    listen       80;
    server_name  localhost;
    client_max_body_size 1024m;

    # 是否启动 gzip 压缩
    gzip  on;
    # 需要压缩的常见静态资源
    gzip_types text/plain application/javascript application/x-javascript text/css application/xml text/javascript application/x-httpd-php image/jpeg image/gif image/png;
    # 如果文件大于 1k 就启动压缩
    gzip_min_length 1k;
    # 缓冲区
    gzip_buffers 4 16k;
    # 压缩的等级
    gzip_comp_level 2;
    # access_log  /var/log/nginx/host.access.log  main;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP  $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        # web history 模式 404
        try_files $uri $uri/ /index.html;
    }

    location /orion-visor/api {
        proxy_pass    http://localhost:9200/orion-visor/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP  $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

     location /orion-visor/keep-alive {
        proxy_pass    http://localhost:9200/orion-visor/keep-alive;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 3600s;
        proxy_send_timeout 3600s;
    }

    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

}
```

### 部署

```
复制 orion-visor/orion-visor-ui/dist 到 /usr/share/nginx/html
复制 orion-visor/orion-visor-launch/target/orion-visor-launch.jar 到 /data/orion
# 启动后台服务
nohup java -jar orion-visor-launch.jar --spring.profiles.active=prod 2>&1 &
# 启动 nginx
service nginx start
```

### 测试访问

在浏览器中输入 http://localhost 访问  
账号: `admin`  
密码: `admin`
