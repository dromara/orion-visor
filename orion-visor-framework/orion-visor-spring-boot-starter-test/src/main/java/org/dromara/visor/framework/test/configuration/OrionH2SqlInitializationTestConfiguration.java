/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.test.configuration;

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
@Lazy(false)
@Profile("unit-test")
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(AbstractScriptDatabaseInitializer.class)
@ConditionalOnSingleCandidate(DataSource.class)
@ConditionalOnClass(name = "org.springframework.jdbc.datasource.init.DatabasePopulator")
@EnableConfigurationProperties(SqlInitializationProperties.class)
public class OrionH2SqlInitializationTestConfiguration {

    /**
     * @return 数据源脚本初始化器
     */
    @Bean
    public DataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
                                                                                   SqlInitializationProperties initializationProperties) {
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
