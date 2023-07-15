package com.orion.ops.framework.mybatis.core.generator;

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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        @NotNull
        String outputDir = "D:/MP/";
        @NotNull
        String author = Const.ORION_AUTHOR;
        @NotEmpty
        String[] tables = {"system_role", "system_user_role", "system_menu", "system_role_menu"};
        // 表业务注释 需要和表一一对应 null则为表注释
        @NotEmpty
        String[] comment = {"角色", "用户角色关联", "菜单", "角色菜单关联"};
        // 模块
        @NotNull
        String module = "infra";
        // jdbc 配置 - 使用配置文件
        File yamlFile = new File("orion-ops-launch/src/main/resources/application-dev.yaml");
        YmlExt yaml = YmlExt.load(yamlFile);
        String url = yaml.getValue("spring.datasource.druid.url");
        String username = yaml.getValue("spring.datasource.druid.username");
        String password = yaml.getValue("spring.datasource.druid.password");

        // 执行
        runGenerator(outputDir, author,
                url, username, password,
                tables, comment, module);
    }

    /**
     * 代码生成
     */
    private static void runGenerator(String outputDir,
                                     String author,
                                     String url,
                                     String username,
                                     String password,
                                     String[] tables,
                                     String[] comment,
                                     String module) {
        // 创建引擎
        VelocityTemplateEngine engine = getEngine(tables, comment);

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
     * @param tables  表
     * @param comment 表注释
     * @return
     */
    private static VelocityTemplateEngine getEngine(String[] tables, String[] comment) {
        if (tables.length != comment.length) {
            throw new IllegalArgumentException("表称与业务注释长度不匹配");
        }
        // 业务注释
        Map<String, String> tableComment = new HashMap<>();
        for (int i = 0; i < tables.length; i++) {
            tableComment.put(tables[i], comment[i]);
        }
        return new VelocityTemplateEngine(tableComment);
    }

    /**
     * 获取全局配置
     *
     * @param outputDir 输出地址
     * @param author    作者
     * @return config
     */
    private static GlobalConfig getGlobalConfig(String outputDir, String author) {
        // 全局配置
        GlobalConfig gbConfig = new GlobalConfig.Builder()
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
        return gbConfig;
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
        DataSourceConfig dsConfig = new DataSourceConfig.Builder(url, username, password)
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
        return dsConfig;
    }

    /**
     * 获取策略配置
     *
     * @param tables 生成的表名
     * @return 策略配置
     */
    private static StrategyConfig getStrategyConfig(String[] tables) {
        StrategyConfig stConfig = new StrategyConfig.Builder()
                // 生成的表
                .addInclude(tables)
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
        return stConfig;
    }

    /**
     * 获取包名配置
     *
     * @param module 模块
     * @return 包名配置
     */
    private static PackageConfig getPackageConfig(String module) {
        PackageConfig pkConfig = new PackageConfig.Builder()
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
        return pkConfig;
    }

    /**
     * 获取模板配置
     *
     * @return 模板配置
     */
    private static TemplateConfig getTemplateConfig() {
        TemplateConfig tplConfig = new TemplateConfig.Builder()
                .controller("/templates/orion-controller.java.vm")
                .entity("/templates/orion-entity-do.java.vm")
                .service("/templates/orion-service.java.vm")
                .serviceImpl("/templates/orion-service-impl.java.vm")
                .mapper("/templates/orion-mapper.java.vm")
                .xml("/templates/orion-mapper.xml.vm")
                .build();
        return tplConfig;
    }

    /**
     * 获取注入配置
     *
     * @return 注入配置
     */
    private static InjectionConfig getInjectionConfig() {
        String[][] customFileDefineArr = new String[][]{
                // http 文件
                new String[]{"/templates/orion-controller.http.vm", "%sController.http", "controller"},
                // vo 文件
                new String[]{"/templates/orion-entity-vo.java.vm", "%sVO.java", "entity.vo"},
                // dto 文件
                new String[]{"/templates/orion-entity-dto.java.vm", "%sDTO.java", "entity.dto"},
                // create request 文件
                new String[]{"/templates/orion-entity-request-create.java.vm", "%sCreateRequest.java", "entity.request"},
                // update request 文件
                new String[]{"/templates/orion-entity-request-update.java.vm", "%sUpdateRequest.java", "entity.request"},
                // query request 文件
                new String[]{"/templates/orion-entity-request-query.java.vm", "%sQueryRequest.java", "entity.request"},
                // convert 文件
                new String[]{"/templates/orion-convert.java.vm", "%sConvert.java", "convert"},
                // convert provider 文件
                new String[]{"/templates/orion-convert-provider.java.vm", "%sProviderConvert.java", "convert"},
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
