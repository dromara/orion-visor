/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.framework.test.core.base;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.orion.visor.framework.common.configuration.OrionCommonAutoConfiguration;
import com.orion.visor.framework.datasource.configuration.OrionDataSourceAutoConfiguration;
import com.orion.visor.framework.mybatis.configuration.OrionMybatisAutoConfiguration;
import com.orion.visor.framework.redis.configuration.OrionRedisAutoConfiguration;
import com.orion.visor.framework.test.configuration.OrionH2SqlInitializationTestConfiguration;
import com.orion.visor.framework.test.configuration.OrionMockBeanTestConfiguration;
import com.orion.visor.framework.test.configuration.OrionMockRedisTestConfiguration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单元测试父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/23 15:12
 */
@Commit
@Transactional(rollbackFor = Exception.class)
@ActiveProfiles("unit-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = BaseUnitTest.Application.class)
public class BaseUnitTest {

    @Import({
            // holder
            OrionCommonAutoConfiguration.class,
            // mock
            OrionMockBeanTestConfiguration.class,
            OrionMockRedisTestConfiguration.class,
            // datasource
            OrionDataSourceAutoConfiguration.class,
            DruidDataSourceAutoConfigure.class,
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            OrionH2SqlInitializationTestConfiguration.class,
            // mybatis
            OrionMybatisAutoConfiguration.class,
            MybatisPlusAutoConfiguration.class,
            // redis
            OrionRedisAutoConfiguration.class,
            RedisAutoConfiguration.class,
            RedissonAutoConfiguration.class,
    })
    public static class Application {
    }

}
