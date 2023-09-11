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
import com.orion.lang.utils.ansi.AnsiAppender;
import com.orion.lang.utils.ansi.style.AnsiFont;
import com.orion.lang.utils.ansi.style.color.AnsiForeground;
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
        String module = "asset";
        // 生成的表
        GenTable[] tables = {
                // new GenTable("system_user", "用户", "user")
                //         .vue("user", "user")
                //         .enums(UserStatusEnum.class),
                new GenTable("host", "主机", "host")
                        .vue("asset", "host")
                        .ignoreTest(),
                new GenTable("host_config", "主机配置", "host").ignoreTest(),
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

        // 打印提示信息
        printTips();
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
                .controller("/templates/orion-server-module-controller.java.vm")
                .entity("/templates/orion-server-module-entity-do.java.vm")
                .service("/templates/orion-server-module-service.java.vm")
                .serviceImpl("/templates/orion-server-module-service-impl.java.vm")
                .mapper("/templates/orion-server-module-mapper.java.vm")
                .xml("/templates/orion-server-module-mapper.xml.vm")
                .build();
    }

    /**
     * 获取注入配置
     *
     * @return 注入配置
     */
    private static InjectionConfig getInjectionConfig() {
        String[][] customFileDefineArr = new String[][]{
                // -------------------- 后端 - module --------------------
                // http 文件
                new String[]{"/templates/orion-server-module-controller.http.vm", "${type}Controller.http", "controller"},
                // vo 文件
                new String[]{"/templates/orion-server-module-entity-vo.java.vm", "${type}VO.java", "entity.vo"},
                // create request 文件
                new String[]{"/templates/orion-server-module-entity-request-create.java.vm", "${type}CreateRequest.java", "entity.request.${bizPackage}"},
                // update request 文件
                new String[]{"/templates/orion-server-module-entity-request-update.java.vm", "${type}UpdateRequest.java", "entity.request.${bizPackage}"},
                // query request 文件
                new String[]{"/templates/orion-server-module-entity-request-query.java.vm", "${type}QueryRequest.java", "entity.request.${bizPackage}"},
                // export 文件
                new String[]{"/templates/orion-server-module-entity-export.java.vm", "${type}Export.java", "entity.export"},
                // convert 文件
                new String[]{"/templates/orion-server-module-convert.java.vm", "${type}Convert.java", "convert"},
                // -------------------- 后端 - provider --------------------
                // api 文件
                new String[]{"/templates/orion-server-provider-api.java.vm", "${type}Api.java", "api"},
                // api impl 文件
                new String[]{"/templates/orion-server-provider-api-impl.java.vm", "${type}ApiImpl.java", "api.impl"},
                // dto 文件
                new String[]{"/templates/orion-server-provider-entity-dto.java.vm", "${type}DTO.java", "entity.dto.${bizPackage}"},
                // create dto 文件
                new String[]{"/templates/orion-server-provider-entity-dto-create.java.vm", "${type}CreateDTO.java", "entity.dto.${bizPackage}"},
                // update dto 文件
                new String[]{"/templates/orion-server-provider-entity-dto-update.java.vm", "${type}UpdateDTO.java", "entity.dto.${bizPackage}"},
                // query dto 文件
                new String[]{"/templates/orion-server-provider-entity-dto-query.java.vm", "${type}QueryDTO.java", "entity.dto.${bizPackage}"},
                // convert 文件
                new String[]{"/templates/orion-server-provider-convert.java.vm", "${type}ProviderConvert.java", "convert"},
                // -------------------- 后端 - test --------------------
                // service unit test 文件
                new String[]{"/templates/orion-server-test-service-impl-tests.java.vm", "${type}ServiceImplTests.java", "service.impl"},
                // api unit test 文件
                new String[]{"/templates/orion-server-test-api-impl-tests.java.vm", "${type}ApiImplTests.java", "api.impl"},
                // create table sql 文件
                new String[]{"/templates/orion-server-test-create-table.sql.vm", "create-table-h2-${tableName}.sql", "sql"},
                // -------------------- 前端 --------------------
                // api 文件
                new String[]{"/templates/orion-vue-api.ts.vm", "${feature}.ts", "vue/api/${module}"},
                // router 文件
                new String[]{"/templates/orion-vue-router.ts.vm", "${module}.${feature}.ts", "vue/router/routes/modules"},
                // views index.ts 文件
                new String[]{"/templates/orion-vue-views-index.vue.vm", "index.vue", "vue/views/${module}/${feature}"},
                // form-modal.vue 文件
                new String[]{"/templates/orion-vue-views-components-form-modal.vue.vm", "${feature}-form-modal.vue", "vue/views/${module}/${feature}/components"},
                // table.vue 文件
                new String[]{"/templates/orion-vue-views-components-table.vue.vm", "${feature}-table.vue", "vue/views/${module}/${feature}/components"},
                // enum.types.ts 文件
                new String[]{"/templates/orion-vue-views-types-enum.types.ts.vm", "enum.types.ts", "vue/views/${module}/${feature}/types"},
                // form.rules.ts 文件
                new String[]{"/templates/orion-vue-views-types-form.rules.ts.vm", "form.rules.ts", "vue/views/${module}/${feature}/types"},
                // table.vue 文件
                new String[]{"/templates/orion-vue-views-types-table.columns.ts.vm", "table.columns.ts", "vue/views/${module}/${feature}/types"},
                // menu.sql 文件
                new String[]{"/templates/orion-sql-menu.sql.vm", "${feature}-menu.sql", "sql"},
        };

        // 构建文件
        List<CustomFile> customerFiles = Arrays.stream(customFileDefineArr)
                .map(s -> new CustomFile.Builder()
                        // 覆盖文件
                        .enableFileOverride()
                        // 模板路径
                        .templatePath(s[0])
                        // 文件名
                        .fileName(s[1])
                        // 包名
                        .packageName(s[2])
                        .build())
                .collect(Collectors.toList());

        // 注入配置
        return new InjectionConfig.Builder()
                // 自定义 文件
                .customFile(customerFiles)
                // 构建
                .build();
    }

    /**
     * 打印提示信息
     */
    private static void printTips() {
        String line = AnsiAppender.create()
                .append(AnsiForeground.BRIGHT_GREEN.and(AnsiFont.BOLD), "\n:: 代码生成完毕 ^_^ ::\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 后端代码复制后请先 clean 模块父工程\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- 后端代码复制后请先执行单元测试检测是否正常\n")
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "- vue 代码需要注意同一模块的 router 需要自行合并\n")
                .toString();
        System.out.print(line);
    }

}
