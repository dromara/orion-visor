package com.orion.ops.framework.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orion.ops.framework.mybatis.handler.FieldFillHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * mybatis 配置类
 * TODO 扫描的包
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:35
 */
@AutoConfiguration
@MapperScan(value = "com.orion.ops.module.*.dao", lazyInitialization = "true")
public class OrionMybatisAutoConfiguration {

    /**
     * @return 字段填充元数据处理器
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        return new FieldFillHandler();
    }

}
