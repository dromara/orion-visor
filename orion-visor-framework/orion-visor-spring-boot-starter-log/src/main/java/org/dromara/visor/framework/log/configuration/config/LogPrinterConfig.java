/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.framework.log.configuration.config;

import lombok.Data;
import org.dromara.visor.framework.common.utils.ConfigUtils;
import org.dromara.visor.framework.log.core.enums.LogPrinterMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 日志打印配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/28 22:36
 */
@Data
@ConfigurationProperties("orion.logging.printer")
public class LogPrinterConfig {

    /**
     * 类型
     */
    private LogPrinterMode mode;

    /**
     * 字段配置
     */
    private LogPrinterFieldConfig field;

    /**
     * 显示的请求头
     */
    private List<String> headers;

    /**
     * 切面表达式
     */
    private String expression;

    public void setHeaders(List<String> headers) {
        this.headers = ConfigUtils.parseStringList(headers, String::toLowerCase);
    }

    @Data
    public static class LogPrinterFieldConfig {

        /**
         * 忽略的字段
         */
        private List<String> ignore;

        /**
         * 脱敏的字段
         */
        private List<String> desensitize;

        public void setIgnore(List<String> ignore) {
            this.ignore = ConfigUtils.parseStringList(ignore);
        }

        public void setDesensitize(List<String> desensitize) {
            this.desensitize = ConfigUtils.parseStringList(desensitize);
        }

    }

}
