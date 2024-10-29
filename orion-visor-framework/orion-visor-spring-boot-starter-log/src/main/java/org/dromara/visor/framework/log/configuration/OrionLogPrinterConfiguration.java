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
package org.dromara.visor.framework.log.configuration;

import org.dromara.visor.framework.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.common.constant.BeanOrderConst;
import org.dromara.visor.framework.log.configuration.config.LogPrinterConfig;
import org.dromara.visor.framework.log.core.interceptor.LogPrinterInterceptor;
import org.dromara.visor.framework.log.core.interceptor.PrettyLogPrinterInterceptor;
import org.dromara.visor.framework.log.core.interceptor.RowLogPrinterInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * 全局日志打印配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/16 18:18
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_LOG)
@EnableConfigurationProperties(LogPrinterConfig.class)
public class OrionLogPrinterConfiguration {

    @Resource
    private LogPrinterConfig config;

    /**
     * @return 美化日志打印器
     */
    @Bean(initMethod = "init")
    @ConditionalOnProperty(value = "orion.logging.printer.mode", havingValue = "pretty")
    public LogPrinterInterceptor prettyPrinter() {
        return new PrettyLogPrinterInterceptor(config);
    }

    /**
     * @return 单行日志打印器
     */
    @Bean(initMethod = "init")
    @ConditionalOnProperty(value = "orion.logging.printer.mode", havingValue = "row")
    public LogPrinterInterceptor rowPrinter() {
        return new RowLogPrinterInterceptor(config);
    }

    /**
     * @param logPrinterInterceptor logPrinterInterceptor
     * @return 日志打印切面
     */
    @Bean
    @ConditionalOnBean(LogPrinterInterceptor.class)
    public AspectJExpressionPointcutAdvisor logPrinterAdvisor(LogPrinterInterceptor logPrinterInterceptor) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(config.getExpression());
        advisor.setAdvice(logPrinterInterceptor);
        advisor.setOrder(BeanOrderConst.LOG_PRINT_ASPECT);
        return advisor;
    }

}
