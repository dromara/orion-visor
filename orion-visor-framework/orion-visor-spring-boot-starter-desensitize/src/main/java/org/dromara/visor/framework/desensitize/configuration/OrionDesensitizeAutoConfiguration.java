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
package org.dromara.visor.framework.desensitize.configuration;

import org.dromara.visor.framework.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.desensitize.core.filter.DesensitizeValueFilter;
import org.dromara.visor.framework.desensitize.core.serializer.DesensitizeJsonSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 数据脱敏置类
 * <p>
 * 前置需要装配 orion-visor-spring-boot-starter-web
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 16:55
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_DESENSITIZE)
public class OrionDesensitizeAutoConfiguration {

    /**
     * 用于参数打印
     * - 全局日志打印
     * - 操作日志参数
     *
     * @return fastjson 序列化脱敏过滤器
     */
    @Primary
    @Bean
    public DesensitizeValueFilter desensitizeValueFilter() {
        return new DesensitizeValueFilter();
    }

    /**
     * 用于 HTTP 响应序列化
     *
     * @return jackson 序列化脱敏序列化
     */
    @Bean
    @ConditionalOnBean(MappingJackson2HttpMessageConverter.class)
    public DesensitizeJsonSerializer desensitizeJsonSerializer() {
        return new DesensitizeJsonSerializer();
    }

}
