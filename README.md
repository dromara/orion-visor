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
com.orion.ops.framework.mybatis.core.generator.CodeGenerators

// 生成的表为 system_role, 业务注释为 '角色', 业务包为 role
Template.create("system_role", "角色", "role")
        // 生成 api
        .enableProviderApi()
        // 不生成单元测试
        .disableUnitTest()
        // 生成缓存
        .cache("user:role", "角色缓存")
        // 缓存过期时间 1 DAY
        .expire(1, TimeUnit.DAYS)
        // 生成 vue 文件, 一级业务包为 user, 二级业务包为 role (前端命名只能使用脊柱命名法)
        .vue("user", "role")
        // 前端使用抽屉表单 (多字段使用)
        .enableDrawerForm()
        // 前端支持卡片或 (多字段使用)
        .enableCardView()
        // 前端支持多选
        .enableRowSelection()
        // 生成字典配置 配置项为 'dictValueType', 替换的字段为 'value_type'
        .dict("dictValueType", "value_type")
        // 设置字段名称
        .fields("STRING", "INTEGER", "DECIMAL", "BOOLEAN", "COLOR")
        // 设置字典值
        .value(1, 2, 3, 4, 5)
        // 设置字典值描述
        .labels("字符串", "整数", "小数", "布尔值", "颜色")
        // 设置额外值 color
        .color("blue", "gray", "red", "green", "white")
        .build();
```

