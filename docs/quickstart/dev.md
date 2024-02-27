### 所需环境

* JDK 1.8
* Mysql 8.0(+)
* Redis 5.0.5(+)
* Node 16.16.0(+)
* Maven 3.5.4(+)

⚡ maven 推荐使用阿里云 mirror   
⚡ npm 建议使用淘宝镜像 `npm config set registry https://registry.npmmirror.com/`  

### 配置

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

3. 修改后端配置

```
# 修改配置文件 (mysql, redis, secret-key)
orion-ops-pro/orion-ops-launch/src/main/resources/application-prod.yaml

# 进入代码目录
cd orion-ops-pro
# 编译
mvn -U clean install -DskipTests
```

4. 修改前端配置

```
# 进入代码目录
cd orion-ops-pro/orion-ops-ui
# 下载 pnpm
npm i -g pnpm
# 下载依赖
pnpm i
# 运行
pnpm dev
```   

### 测试访问

启动前端后会自动打开浏览器 输入  
账号: `admin`  
密码: `admin`

### 文档

文档只有在 `dev` 环境才可以访问

- swagger 文档       http://127.0.0.1:9200/doc.html
- druid console     http://127.0.0.1:9200/druid/index.html
- actuator endpoint http://127.0.0.1:9200/actuator
- admin console     http://127.0.0.1:9200/admin
