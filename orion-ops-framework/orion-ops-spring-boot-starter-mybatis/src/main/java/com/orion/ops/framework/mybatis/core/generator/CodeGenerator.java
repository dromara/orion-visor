package com.orion.ops.framework.mybatis.core.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
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
 * @author Jiahang Li
 */
public class CodeGenerator {

    public static void main(String[] args) {
        String outputDir = "D:/MP/";
        String author = Const.ORION_AUTHOR;
        // 表名
        String[] tables = {"user_info"};
        // 模块
        String module = "user";
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

        // 全局配置
        GlobalConfig gbConfig = new GlobalConfig.Builder()
                // 设置作者
                .author(author)
                // 生成路径
                .outputDir(outputDir)
                // 生成 swagger 注解
                .enableSwagger()
                // 生成 spring doc 注解
                .enableSpringdoc()
                // date类型
                .dateType(DateType.ONLY_DATE)
                // 注释时间
                .commentDate("yyyy-MM-dd")
                // 构建
                .build();

        // 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig.Builder(url, username, password)
                // 转换器
                .typeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
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

        // 策略配置
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
                // 构建
                .build();

        // 包名策略配置
        PackageConfig pkConfig = new PackageConfig.Builder()
                // 声明父包
                .parent("com.orion.ops.module." + module)
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

        // 整合配置
        AutoGenerator ag = new AutoGenerator(dsConfig)
                // 整合全局配置
                .global(gbConfig)
                // 整合表名配置
                .strategy(stConfig)
                // 整合包名策略
                .packageInfo(pkConfig)
                // TODO 自定义convert文件 request VO
                // .injection()
                // TODO 自定义模板以及convert文件
                // .template()
                ;

        // 执行
        ag.execute();
    }

}
