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
package com.orion.visor.module.infra.define.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用认证配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/5 18:26
 */
@Data
@Component
@ConfigurationProperties("app.authentication")
public class AppAuthenticationConfig {

    /**
     * 是否允许多端登录
     */
    private Boolean allowMultiDevice;

    /**
     * 是否允许凭证续签
     */
    private Boolean allowRefresh;

    /**
     * 凭证续签最大次数
     */
    private Integer maxRefreshCount;

    /**
     * 登录失败发送站内信阈值
     */
    private Integer loginFailedSendThreshold;

    /**
     * 登录失败锁定次数
     */
    private Integer loginFailedLockCount;

    /**
     * 登录失败锁定时间 (分)
     */
    private Integer loginFailedLockTime;

}
