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
package org.dromara.visor.framework.biz.operator.log.configuration;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.dromara.visor.framework.biz.operator.log.configuration.config.OperatorLogConfig;
import org.dromara.visor.framework.biz.operator.log.core.aspect.OperatorLogAspect;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkServiceDelegate;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogFiller;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.common.json.filter.FieldDesensitizeFilter;
import org.dromara.visor.framework.common.json.filter.FieldIgnoreFilter;
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
