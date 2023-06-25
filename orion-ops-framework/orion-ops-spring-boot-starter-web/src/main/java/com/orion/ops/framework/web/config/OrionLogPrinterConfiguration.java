package com.orion.ops.framework.web.config;

import com.orion.ops.framework.common.constant.InterceptorOrderConst;
import com.orion.ops.framework.web.core.interceptor.LogPrintInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 全局日志打印配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 18:18
 */
@AutoConfiguration
public class OrionLogPrinterConfiguration {

    @Bean
    public LogPrintInterceptor logPrintInterceptor() {
        return new LogPrintInterceptor();
    }

    @Bean
    public AspectJExpressionPointcutAdvisor logPrinterAdvisor(LogPrintInterceptor logPrintInterceptor) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("execution (* com.orion.ops.**.controller.*.*(..)) && !@annotation(com.orion.ops.framework.common.annotation.IgnoreLog)");
        advisor.setAdvice(logPrintInterceptor);
        advisor.setOrder(InterceptorOrderConst.LOG_FILTER);
        return advisor;
    }

}
