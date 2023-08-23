package com.orion.ops.framework.test.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.AbstractScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * 单元测试 H2 数据库 DML 初始化脚本
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/23 17:17
 */
@Profile("unit-test")
@Lazy(value = false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AbstractScriptDatabaseInitializer.class)
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnClass(name = "org.springframework.jdbc.datasource.init.DatabasePopulator")
@EnableConfigurationProperties(SqlInitializationProperties.class)
public class OrionH2SqlInitializationTestConfiguration {

    /**
     * 数据源脚本初始化 Bean
     */
    @Bean
    public DataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
                                                                                   SqlInitializationProperties initializationProperties) {
        // TODO 看看正常情况下会不会有
        // 初始化配置
        DatabaseInitializationSettings settings = new DatabaseInitializationSettings();
        settings.setSchemaLocations(initializationProperties.getSchemaLocations());
        settings.setDataLocations(initializationProperties.getDataLocations());
        settings.setContinueOnError(initializationProperties.isContinueOnError());
        settings.setSeparator(initializationProperties.getSeparator());
        settings.setEncoding(initializationProperties.getEncoding());
        settings.setMode(initializationProperties.getMode());
        // 初始化
        return new DataSourceScriptDatabaseInitializer(dataSource, settings);
    }

}
