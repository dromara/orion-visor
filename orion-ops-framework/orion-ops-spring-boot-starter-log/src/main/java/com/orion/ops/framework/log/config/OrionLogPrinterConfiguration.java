package com.orion.ops.framework.log.config;

import com.orion.ops.framework.common.constant.InterceptorOrderConst;
import com.orion.ops.framework.log.core.config.LogPrinterConfig;
import com.orion.ops.framework.log.core.interceptor.LogPrinterInterceptor;
import com.orion.ops.framework.log.core.interceptor.PrettyLogPrinterInterceptor;
import com.orion.ops.framework.log.core.interceptor.RowLogPrinterInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 全局日志打印配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 18:18
 */
@AutoConfiguration
@EnableConfigurationProperties(LogPrinterConfig.class)
public class OrionLogPrinterConfiguration {

    /**
     * @param config config
     * @return 美化日志打印器
     */
    @Bean(initMethod = "init")
    @ConditionalOnProperty(value = "logging.printer.mode", havingValue = "pretty")
    public LogPrinterInterceptor prettyPrinter(LogPrinterConfig config) {
        return new PrettyLogPrinterInterceptor(config);
    }

    /**
     * @param config config
     * @return 单行日志打印器
     */
    @Bean(initMethod = "init")
    @ConditionalOnProperty(value = "logging.printer.mode", havingValue = "row")
    public LogPrinterInterceptor rowPrinter(LogPrinterConfig config) {
        return new RowLogPrinterInterceptor(config);
    }

    /**
     * @param logPrinterInterceptor logPrinterInterceptor
     * @param expression            切面表达式
     * @return 日志打印切面
     */
    @Bean
    @ConditionalOnBean(LogPrinterInterceptor.class)
    public AspectJExpressionPointcutAdvisor logPrinterAdvisor(LogPrinterInterceptor logPrinterInterceptor,
                                                              @Value("${logging.printer.expression}") String expression) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(expression);
        advisor.setAdvice(logPrinterInterceptor);
        advisor.setOrder(InterceptorOrderConst.LOG_FILTER);
        return advisor;
    }

}
