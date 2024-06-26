### 所需环境

* jdk 1.8
* mysql 8.0.+
* redis 6.0.+
* maven 3.5.+
* node 18.12.+
* pnpm 9.1.+

⚡ maven 推荐使用阿里云 mirror   
⚡ npm 建议使用淘宝镜像 `npm config set registry https://registry.npmmirror.com/`  
⚡ pnpm 建议使用淘宝镜像 `pnpm config set registry https://registry.npmmirror.com/`

### 拉取代码

```
# github
git clone https://github.com/lijiahangmax/orion-visor
# gitee
git clone https://gitee.com/lijiahangmax/orion-visor
```

### 初始化数据库

```
# 执行脚本
orion-visor/sql/init-1-schema-databases.sql
orion-visor/sql/init-2-schema-tables.sql
orion-visor/sql/init-3-schema-quartz.sql
orion-visor/sql/init-4-data.sql
```

### 修改后端配置

```
# 修改配置文件 (mysql, redis, secret-key)
orion-visor/orion-visor-launch/src/main/resources/application-prod.yaml

# 进入代码目录
cd orion-visor
# 编译
mvn -U clean install -DskipTests
# 启动
com.orion.visor.launch.LaunchApplication
```

### 修改前端配置

```shell
# 进入代码目录
cd orion-visor/orion-visor-ui
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
