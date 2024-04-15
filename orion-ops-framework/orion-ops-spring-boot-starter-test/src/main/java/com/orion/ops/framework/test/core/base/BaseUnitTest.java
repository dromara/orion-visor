package com.orion.ops.framework.test.core.base;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.orion.ops.framework.common.configuration.OrionCommonAutoConfiguration;
import com.orion.ops.framework.datasource.configuration.OrionDataSourceAutoConfiguration;
import com.orion.ops.framework.mybatis.configuration.OrionMybatisAutoConfiguration;
import com.orion.ops.framework.redis.configuration.OrionRedisAutoConfiguration;
import com.orion.ops.framework.test.configuration.OrionH2SqlInitializationTestConfiguration;
import com.orion.ops.framework.test.configuration.OrionMockBeanTestConfiguration;
import com.orion.ops.framework.test.configuration.OrionMockRedisTestConfiguration;
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
