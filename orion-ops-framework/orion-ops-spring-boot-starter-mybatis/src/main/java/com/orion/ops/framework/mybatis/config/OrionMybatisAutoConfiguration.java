package com.orion.ops.framework.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import com.orion.ops.framework.common.constant.FilterOrderConst;
import com.orion.ops.framework.common.web.filter.FilterCreator;
import com.orion.ops.framework.common.security.SecurityHolder;
import com.orion.ops.framework.mybatis.core.cache.CacheClearFilter;
import com.orion.ops.framework.mybatis.core.handler.FieldFillHandler;
import com.orion.ops.framework.mybatis.core.utils.DomainFillUtils;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * mybatis 配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:35
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_MYBATIS)
@MapperScan(value = "com.orion.ops.module.*.dao", annotationClass = Mapper.class, lazyInitialization = "true")
public class OrionMybatisAutoConfiguration {

    /**
     * @return 字段填充元数据处理器
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(SecurityHolder securityHolder) {
        // 设置填充工具参数
        DomainFillUtils.setSecurityHolder(securityHolder);
        return new FieldFillHandler();
    }

    /**
     * 注册 乐观锁插件
     *
     * @return 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * @return mybatis 缓存清理过滤器
     */
    @Bean
    public FilterRegistrationBean<CacheClearFilter> rowCacheClearFilterBean() {
        return FilterCreator.create(new CacheClearFilter(), FilterOrderConst.MYBATIS_CACHE_CLEAR_FILTER);
    }

}
