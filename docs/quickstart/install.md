### 所需环境

* JDK 1.8
* Mysql 8.0(+)
* Redis 5.0.5(+)
* Node 16.16.0(+)
* Maven 3.5.4(+)
* Nginx

⚡ maven 推荐使用阿里云 mirror   
⚡ npm 建议使用淘宝镜像 `npm config set registry https://registry.npmmirror.com/`

### 构建

1. 拉取代码

```
# github
git clone https://github.com/lijiahangmax/orion-ops-pro
# gitee
git clone https://gitee.com/lijiahangmax/orion-ops-pro
```

2. 初始化数据库

```
# 执行脚本
orion-ops-pro/sql/init-1-schema-databases.sql
orion-ops-pro/sql/init-2-schema-tables.sql
orion-ops-pro/sql/init-3-data.sql
```

3. 构建后端代码

```
# 修改配置文件 (mysql, redis, secret-key)
orion-ops-pro/orion-ops-launch/src/main/resources/application-prod.yaml

# 进入代码目录
cd orion-ops-pro
# 编译
mvn -U clean install -DskipTests
```   

4. 构建前端代码

```
# 进入代码目录
cd orion-ops-pro/orion-ops-ui
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

    location /orion/api {
        proxy_pass    http://localhost:9200/orion/api;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP  $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

     location /orion/keep-alive {
        proxy_pass    http://localhost:9200/orion/keep-alive;
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
复制 orion-ops-pro/orion-ops-ui 到 /usr/share/nginx/html
复制 orion-ops-pro/orion-ops-launch/target/orion-ops-launch.jar 到 /data/orion
# 启动后台服务
nohup java -jar orion-ops-launch.jar --spring.profiles.active=prod 2>&1 &
# 启动 nginx
service nginx start
```

### 测试访问

在浏览器中输入 http://localhost 访问  
账号: `admin`  
密码: `admin`
