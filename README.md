### orion-ops-pro (orion-ops重构版) 

### 开发中

##### 项目结构
```
orion-ops-pro 仓库主目录
    orion-ops-dependencies        依赖模块 项目依赖版本的统一管理
    orion-ops-framework           架构模块 包含了项目所有的配置
    orion-ops-launch              启动模块 项目主容器用于服务的启动
    orion-ops-module-asset        资产模块
    orion-ops-module-infra        基建模块
    orion-ops-ui                  前端代码
```


##### sql脚本
```
orion-ops-pro
    - sql
        - init-schema.sql (初始化表结构)
        - init-data.sql (初始化数据, 用户, 角色, 菜单)
        - 常用.sql (创建基本表结构)
```


##### 本地启动

```
在 release 1.0.0 之前不准备发布 orion-kit 1.0.6
需要开发者在本地拉取 orion-kit/dev 并且手动构建
https://gitee.com/lijiahangmax/orion-kit

# 构建 orion-kit
切换 dev 分支
mvn clean install -U -DskipTests

# 配置后端
修改 application-dev.yaml 的 mysql, redis 配置

# 启动后端
orion-ops-launch 
IDEA 启动 com.orion.ops.launch.LaunchApplication

# 启动前端
orion-ops-ui 
npm i
npm run dev
```

#### 代码生成器
```
生成通用 controller entity service dao api convert http vue ts sql junit 

代码位置 
com.orion.ops.launch.generator.CodeGenerator

// 生成的表为 system_role, 业务注释为 '角色', 业务包为 role
new GenTable("system_role", "角色", "role")
        // 忽略生成对外 api
        .ignoreApi()
        // 忽略生成单元测试
        .ignoreTest()
        // 生成 vue 文件, 一级业务包为 user, 二级业务包为 role (前端命名只能使用脊柱命名法)
        .vue("user", "role")
        // 前端代码生成的枚举对象 可变参数
        .enums(RoleStatusEnum.class);
```

