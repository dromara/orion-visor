package com.orion.ops.framework.mybatis.core.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.ext.yml.YmlExt;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;

/**
 * 代码生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/4/20 10:33
 */
public class CodeGenerator {

    public static void main(String[] args) {
        String outputDir = "D:/MP/";
        String author = Const.ORION_AUTHOR;
        // 表名
        String[] tables = {"test_table"};
        // 模块
        String module = "infra";
        // 连接
        File yamlFile = new File("orion-ops-launch/src/main/resources/application-dev.yaml");
        YmlExt yaml = YmlExt.load(yamlFile);
        String url = yaml.getValue("spring.datasource.druid.url");
        String username = yaml.getValue("spring.datasource.druid.username");
        String password = yaml.getValue("spring.datasource.druid.password");

        // 执行
        runGenerator(outputDir, author,
                url, username, password,
                tables,
                module);
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
                                     String module) {
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
        ag.execute();
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
                .typeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        if (fieldType.toLowerCase().contains("bit")) {
                            return DbColumnType.INTEGER;
                        }
                        if (fieldType.toLowerCase().contains("tinyint")) {
                            return DbColumnType.INTEGER;
                        }
                        return super.processTypeConvert(globalConfig, fieldType);
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
                .entity("/templates/orion-domain.java.vm")
                .service("/templates/orion-service.java.vm")
                .serviceImpl("/templates/orion-service-impl.java.vm")
                .mapper("/templates/orion-dao.java.vm")
                .xml("/templates/orion-mapper.xml")
                .build();
        return tplConfig;
    }

    /**
     * 获取注入配置
     *
     * @return 注入配置
     */
    private static InjectionConfig getInjectionConfig() {
        // vo 文件
        CustomFile voFile = new CustomFile.Builder()
                .enableFileOverride()
                .templatePath("/templates/orion-vo.java.vm")
                .fileName("VO.java")
                .packageName("vo")
                .build();

        // dto 文件
        CustomFile dtoFile = new CustomFile.Builder()
                .enableFileOverride()
                .templatePath("/templates/orion-dto.java.vm")
                .fileName("DTO.java")
                .packageName("dto")
                .build();

        // request 文件
        CustomFile requestFile = new CustomFile.Builder()
                .enableFileOverride()
                .templatePath("/templates/orion-request.java.vm")
                .fileName("Request.java")
                .packageName("request")
                .build();

        // convert 文件
        CustomFile convertFile = new CustomFile.Builder()
                .enableFileOverride()
                .templatePath("/templates/orion-convert.java.vm")
                .fileName("Convert.java")
                .packageName("convert")
                .build();

        // 注入配置
        InjectionConfig injection = new InjectionConfig.Builder()
                // vo 文件
                .customFile(voFile)
                // dto 文件
                .customFile(dtoFile)
                // request 文件
                .customFile(requestFile)
                // convert 文件
                .customFile(convertFile)
                // 构建
                .build();
        return injection;
    }

}
