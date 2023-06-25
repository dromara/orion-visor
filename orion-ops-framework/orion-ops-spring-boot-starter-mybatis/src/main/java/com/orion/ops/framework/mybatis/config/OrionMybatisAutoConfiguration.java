package com.orion.ops.framework.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orion.ops.framework.common.constant.FilterOrderConst;
import com.orion.ops.framework.common.filter.FilterCreator;
import com.orion.ops.framework.mybatis.cache.RowCacheClearFilter;
import com.orion.ops.framework.mybatis.handler.FieldFillHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

    /**
     * @return mybatis 缓存清理过滤器
     */
    @Bean
    public FilterRegistrationBean<RowCacheClearFilter> rowCacheClearFilterBean() {
        return FilterCreator.create(new RowCacheClearFilter(), FilterOrderConst.MYBATIS_CACHE_CLEAR_FILTER);
    }

}
