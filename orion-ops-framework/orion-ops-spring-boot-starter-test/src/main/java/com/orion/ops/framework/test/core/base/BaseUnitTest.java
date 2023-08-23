package com.orion.ops.framework.test.core.base;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.orion.ops.framework.datasource.config.OrionDataSourceAutoConfiguration;
import com.orion.ops.framework.mybatis.config.OrionMybatisAutoConfiguration;
import com.orion.ops.framework.test.config.OrionH2SqlInitializationTestConfiguration;
import com.orion.ops.framework.test.config.OrionMockRedisTestConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * 单元测试父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/23 15:12
 */
@Rollback
@ActiveProfiles("unit-test")
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = BaseUnitTest.Application.class)
public class BaseUnitTest {

    @Import({
            // datasource
            OrionDataSourceAutoConfiguration.class,
            DataSourceAutoConfiguration.class,
            DataSourceTransactionManagerAutoConfiguration.class,
            OrionH2SqlInitializationTestConfiguration.class,
            DruidDataSourceAutoConfigure.class,
            // mybatis
            OrionMybatisAutoConfiguration.class,
            MybatisPlusAutoConfiguration.class,
            // redis
            OrionMockRedisTestConfiguration.class,
    })
    public static class Application {
    }

}
