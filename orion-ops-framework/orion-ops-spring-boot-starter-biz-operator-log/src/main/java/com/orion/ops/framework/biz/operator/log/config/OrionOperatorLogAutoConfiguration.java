package com.orion.ops.framework.biz.operator.log.config;

import com.orion.ops.framework.biz.operator.log.core.aspect.OperatorLogAspect;
import com.orion.ops.framework.biz.operator.log.core.config.OperatorLogConfig;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkServiceDelegate;
import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

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
        return new OperatorLogAspect(operatorLogConfig, service);
    }

}
