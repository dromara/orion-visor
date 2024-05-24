package com.orion.visor.framework.biz.operator.log.configuration;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.orion.visor.framework.biz.operator.log.configuration.config.OperatorLogConfig;
import com.orion.visor.framework.biz.operator.log.core.aspect.OperatorLogAspect;
import com.orion.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkServiceDelegate;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogFiller;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.AutoConfigureOrderConst;
import com.orion.visor.framework.common.json.filter.FieldDesensitizeFilter;
import com.orion.visor.framework.common.json.filter.FieldIgnoreFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * 操作日志配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 16:42
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_BIZ_OPERATOR_LOG)
@EnableConfigurationProperties(OperatorLogConfig.class)
public class OrionOperatorLogAutoConfiguration {

    @Resource
    private ValueFilter desensitizeValueFilter;

    /**
     * 操作日志委托类
     *
     * @param service service
     * @return delegate
     */
    @Bean
    @Primary
    @ConditionalOnBean(OperatorLogFrameworkService.class)
    public OperatorLogFrameworkServiceDelegate operatorLogFrameworkService(OperatorLogFrameworkService service) {
        return new OperatorLogFrameworkServiceDelegate(service);
    }

    /**
     * 日志切面
     *
     * @param operatorLogConfig operatorLogConfig
     * @param service           service
     * @return aspect
     */
    @Bean
    @ConditionalOnBean(OperatorLogFrameworkServiceDelegate.class)
    public OperatorLogAspect operatorLogAspect(OperatorLogConfig operatorLogConfig,
                                               OperatorLogFrameworkService service) {
        // 参数过滤器
        SerializeFilter[] serializeFilters = new SerializeFilter[]{
                // 忽略字段过滤器
                new FieldIgnoreFilter(operatorLogConfig.getIgnore()),
                // 脱敏字段过滤器
                new FieldDesensitizeFilter(operatorLogConfig.getDesensitize()),
                // 脱敏字段注解过滤器
                desensitizeValueFilter
        };
        // 设置参数到工具类中
        OperatorLogs.setSerializeFilters(serializeFilters);
        OperatorLogFiller.setSerializeFilters(serializeFilters);
        OperatorLogFiller.setOperatorLogConfig(operatorLogConfig);
        return new OperatorLogAspect(service);
    }

}
