package com.orion.ops.launch.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.ext.yml.YmlExt;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/4/20 10:33
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 输出路径
        String outputDir = "D:/MP/";
        // 作者
        String author = Const.ORION_AUTHOR;
        // 模块
        String module = "infra";
        // 生成的表
        GenTable[] tables = {
                // new GenTable("system_user", "用户", "user")
                //         .vue("user", "user")
                //         .enums(UserStatusEnum.class),
                new GenTable("favorite", "收藏", "favorite", true),
        };
        // jdbc 配置 - 使用配置文件
        File yamlFile = new File("orion-ops-launch/src/main/resources/application-dev.yaml");
        YmlExt yaml = YmlExt.load(yamlFile);
        String url = yaml.getValue("spring.datasource.druid.url");
        String username = yaml.getValue("spring.datasource.druid.username");
        String password = yaml.getValue("spring.datasource.druid.password");

        // 执行
        runGenerator(outputDir, author,
                url, username, password,
                tables, module);
    }

    /**
     * 代码生成
     */
    private static void runGenerator(String outputDir,
                                     String author,
                                     String url,
                                     String username,
                                     String password,
                                     GenTable[] tables,
                                     String module) {
        // 创建引擎
        VelocityTemplateEngine engine = getEngine(tables);

        // 获取全局配置
        GlobalConfig globalConfig = getGlobalConfig(outputDir, author);

        // 数据源配置
        DataSourceConfig dataSourceConfig = getDataSourceConfig(url, username, password);

        // 策略配置
        StrategyConfig strategyConfig = getStrategyConfig(tables);

        // 包名配置
        PackageConfig packageConfig = getPackageConfig(module);

        // 模板配置
        TemplateConfig templateConfig = getTemplateConfig();

        // 注入配置
        InjectionConfig injectionConfig = getInjectionConfig();

        // 整合配置
        AutoGenerator ag = new AutoGenerator(dataSourceConfig)
                // 整合全局配置
                .global(globalConfig)
                // 整合表名配置
                .strategy(strategyConfig)
                // 整合包名配置
                .packageInfo(packageConfig)
                // 整合模板配置
                .template(templateConfig)
                // 整合注入配置
                .injection(injectionConfig);

        // 执行
        ag.execute(engine);
    }

    /**
     * 获取渲染引擎
     *
     * @param tables 表
     * @return 渲染引擎
     */
    private static VelocityTemplateEngine getEngine(GenTable[] tables) {
        return new VelocityTemplateEngine(tables);
    }

    /**
     * 获取全局配置
     *
     * @param outputDir 输出地址
     * @param author    作者
     * @return config
     */
    private static GlobalConfig getGlobalConfig(String outputDir, String author) {
        return new GlobalConfig.Builder()
                // 设置作者
                .author(author)
                // 生成路径
                .outputDir(outputDir)
                // 生成 spring doc 注解
                .enableSpringdoc()
                // date类型
                .dateType(DateType.ONLY_DATE)
                // 注释时间
                .commentDate("yyyy-M-d HH:mm")
                // 构建
                .build();
    }

    /**
     * 获取数据源配置
     *
     * @param url      url
     * @param username username
     * @param password password
     * @return 数据源配置
     */
    private static DataSourceConfig getDataSourceConfig(String url, String username, String password) {
        return new DataSourceConfig.Builder(url, username, password)
                // 转换器
                .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    switch (metaInfo.getJdbcType().TYPE_CODE) {
                        case Types.BIT:
                        case Types.TINYINT:
                            return DbColumnType.INTEGER;
                        default:
                            return typeRegistry.getColumnType(metaInfo);
                    }
                })
                // 查询器
                .dbQuery(new MySqlQuery())
                // 构建
                .build();
    }

    /**
     * 获取策略配置
     *
     * @param tables 生成的表名
     * @return 策略配置
     */
    private static StrategyConfig getStrategyConfig(GenTable[] tables) {
        String[] tableNames = Arrays.stream(tables)
                .map(GenTable::getTableName)
                .toArray(String[]::new);
        // 策略配置
        return new StrategyConfig.Builder()
                // 生成的表
                .addInclude(tableNames)
                // 全局大写命名
                .enableCapitalMode()
                // 实体配置
                .entityBuilder()
                // 下滑线转驼峰命名策略
                .naming(NamingStrategy.underline_to_camel)
                // 实体父类
                .superClass(BaseDO.class)
                // 实体父类字段
                .addSuperEntityColumns("create_time", "update_time", "creator", "updater", "deleted")
                // 启用lombok
                .enableLombok()
                // 主键策略
                .idType(IdType.AUTO)
                // 添加字段注解
                .enableTableFieldAnnotation()
                // 实体文件名称
                .formatFileName("%sDO")
                // 覆盖实体文件
                .enableFileOverride()
                // mapper 配置
                .mapperBuilder()
                // dao 添加 @Mapper
                .mapperAnnotation(Mapper.class)
                // mapper 父类
                .superClass(IMapper.class)
                // 生成 BaseResultMap
                .enableBaseResultMap()
                // 生成 BaseColumnList
                .enableBaseColumnList()
                // mapper 文件名称
                .formatMapperFileName("%sDAO")
                // xml 文件名称
                .formatXmlFileName("%sMapper")
                // 覆盖 mapper 文件
                .enableFileOverride()
                // controller 配置
                .controllerBuilder()
                // controller 文件名称
                .formatFileName("%sController")
                // 脊柱命名法
                .enableHyphenStyle()
                // @RestController
                .enableRestStyle()
                // 覆盖 controller 文件
                .enableFileOverride()
                // service 配置
                .serviceBuilder()
                // 覆盖 service 文件
                .enableFileOverride()
                // service 名称
                .formatServiceFileName("%sService")
                // service impl 名称
                .formatServiceImplFileName("%sServiceImpl")
                // 构建
                .build();
    }

    /**
     * 获取包名配置
     *
     * @param module 模块
     * @return 包名配置
     */
    private static PackageConfig getPackageConfig(String module) {
        return new PackageConfig.Builder()
                // 声明父包
                .parent("com.orion.ops.module")
                // 模块名称
                .moduleName(module)
                // 实体类的包
                .entity("entity.domain")
                // 映射接口的包
                .mapper("dao")
                // 映射文件的包
                .xml("mapper")
                // service接口的包
                .service("service")
                // serviceImpl接口的包
                .serviceImpl("service.impl")
                // controller接口的包
                .controller("controller")
                // 构建
                .build();
    }

    /**
     * 获取模板配置
     *
     * @return 模板配置
     */
    private static TemplateConfig getTemplateConfig() {
        return new TemplateConfig.Builder()
                .controller("/templates/orion-server-controller.java.vm")
                .entity("/templates/orion-server-entity-do.java.vm")
                .service("/templates/orion-server-service.java.vm")
                .serviceImpl("/templates/orion-server-service-impl.java.vm")
                .mapper("/templates/orion-server-mapper.java.vm")
                .xml("/templates/orion-server-mapper.xml.vm")
                .build();
    }

    /**
     * 获取注入配置
     *
     * @return 注入配置
     */
    private static InjectionConfig getInjectionConfig() {
        String[][] customFileDefineArr = new String[][]{
                // -------------------- 后端 - service --------------------
                // http 文件
                new String[]{"/templates/orion-server-controller.http.vm", "%sController.http", "controller"},
                // vo 文件
                new String[]{"/templates/orion-server-entity-vo.java.vm", "%sVO.java", "entity.vo"},
                // create request 文件
                new String[]{"/templates/orion-server-entity-request-create.java.vm", "%sCreateRequest.java", "entity.request.%s"},
                // update request 文件
                new String[]{"/templates/orion-server-entity-request-update.java.vm", "%sUpdateRequest.java", "entity.request.%s"},
                // query request 文件
                new String[]{"/templates/orion-server-entity-request-query.java.vm", "%sQueryRequest.java", "entity.request.%s"},
                // convert 文件
                new String[]{"/templates/orion-server-convert.java.vm", "%sConvert.java", "convert"},
                // -------------------- 后端 - api --------------------
                // api 文件
                new String[]{"/templates/orion-server-api-interface.java.vm", "%sApi.java", "api"},
                // api impl 文件
                new String[]{"/templates/orion-server-api-impl.java.vm", "%sApiImpl.java", "api.impl"},
                // dto 文件
                new String[]{"/templates/orion-server-api-entity-dto.java.vm", "%sDTO.java", "entity.dto.%s"},
                // create dto 文件
                new String[]{"/templates/orion-server-api-entity-dto-create.java.vm", "%sCreateDTO.java", "entity.dto.%s"},
                // update dto 文件
                new String[]{"/templates/orion-server-api-entity-dto-update.java.vm", "%sUpdateDTO.java", "entity.dto.%s"},
                // query dto 文件
                new String[]{"/templates/orion-server-api-entity-dto-query.java.vm", "%sQueryDTO.java", "entity.dto.%s"},
                // convert provider 文件
                new String[]{"/templates/orion-server-api-convert.java.vm", "%sApiConvert.java", "convert"},
                // -------------------- 前端 --------------------
                // vue api 文件
                new String[]{"/templates/orion-vue-api.ts.vm", "${feature}.ts", "vue/api/${module}"},
                // vue router 文件
                new String[]{"/templates/orion-vue-router.ts.vm", "${module}.${feature}.ts", "vue/router/routes/modules"},
                // vue views index.ts 文件
                new String[]{"/templates/orion-vue-views-index.vue.vm", "index.vue", "vue/views/${module}/${feature}"},
                // vue form-modal.vue 文件
                new String[]{"/templates/orion-vue-views-components-form-modal.vue.vm", "${feature}-form-modal.vue", "vue/views/${module}/${feature}/components"},
                // vue table.vue 文件
                new String[]{"/templates/orion-vue-views-components-table.vue.vm", "${feature}-table.vue", "vue/views/${module}/${feature}/components"},
                // vue enum.types.ts 文件
                new String[]{"/templates/orion-vue-views-types-enum.types.ts.vm", "enum.types.ts", "vue/views/${module}/${feature}/types"},
                // vue form.rules.ts 文件
                new String[]{"/templates/orion-vue-views-types-form.rules.ts.vm", "form.rules.ts", "vue/views/${module}/${feature}/types"},
                // vue table.vue 文件
                new String[]{"/templates/orion-vue-views-types-table.columns.ts.vm", "table.columns.ts", "vue/views/${module}/${feature}/types"},
                // sql menu.sql 文件
                new String[]{"/templates/orion-sql-menu.sql.vm", "${feature}-menu.sql", "sql"},
        };

        // 构建文件
        List<CustomFile> customerFiles = Arrays.stream(customFileDefineArr)
                .map(s -> {
                    return new CustomFile.Builder()
                            // 覆盖文件
                            .enableFileOverride()
                            // 模板路径
                            .templatePath(s[0])
                            // 文件名
                            .fileName(s[1])
                            // 包名
                            .packageName(s[2])
                            .build();
                }).collect(Collectors.toList());

        // 注入配置
        InjectionConfig injection = new InjectionConfig.Builder()
                // 自定义 文件
                .customFile(customerFiles)
                // 构建
                .build();
        return injection;
    }

}
