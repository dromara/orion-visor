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
package org.dromara.visor.framework.security.configuration.config;

import lombok.Data;
import org.dromara.visor.framework.common.utils.ConfigUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 安全配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 15:55
 */
@Data
@ConfigurationProperties("orion.security")
public class SecurityConfig {

    /**
     * 加密复杂度
     */
    private Integer passwordEncoderLength;

    /**
     * 匿名接口
     */
    private List<String> permitUrl;

    public SecurityConfig() {
        this.passwordEncoderLength = 4;
    }

    public void setPermitUrl(List<String> permitUrl) {
        this.permitUrl = ConfigUtils.parseStringList(permitUrl);
    }

}
