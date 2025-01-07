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
package org.dromara.visor.framework.biz.operator.log.configuration.config;

import lombok.Data;
import org.dromara.visor.common.utils.ConfigUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 操作日志配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 14:08
 */
@Data
@ConfigurationProperties("orion.operator-log")
public class OperatorLogConfig {

    /**
     * 错误信息长度
     */
    private Integer errorMessageLength;

    /**
     * userAgent 长度
     */
    private Integer userAgentLength;

    /**
     * 忽略记录的字段
     */
    private List<String> ignore;

    /**
     * 需要脱敏的字段
     */
    private List<String> desensitize;

    public OperatorLogConfig() {
        this.errorMessageLength = 255;
        this.userAgentLength = 128;
    }

    public void setIgnore(List<String> ignore) {
        this.ignore = ConfigUtils.parseStringList(ignore);
    }

    public void setDesensitize(List<String> desensitize) {
        this.desensitize = ConfigUtils.parseStringList(desensitize);
    }

}
