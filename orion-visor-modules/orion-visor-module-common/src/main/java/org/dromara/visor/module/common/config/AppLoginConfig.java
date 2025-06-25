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
package org.dromara.visor.module.common.config;

import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;
import org.dromara.visor.common.constant.ConfigKeys;
import org.springframework.stereotype.Component;

/**
 * 应用登录配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/5 18:26
 */
@Component
public class AppLoginConfig {

    /**
     * 凭证有效期(分)
     */
    private final ConfigRef<Integer> loginSessionTime;

    /**
     * 是否允许多端登录
     */
    private final ConfigRef<Boolean> allowMultiDevice;

    /**
     * 是否允许凭证续签
     */
    private final ConfigRef<Boolean> allowRefresh;

    /**
     * 凭证续签最大次数
     */
    private final ConfigRef<Integer> maxRefreshCount;

    /**
     * 凭证续签间隔分
     */
    private final ConfigRef<Integer> refreshInterval;

    /**
     * 登录失败是否锁定
     */
    private final ConfigRef<Boolean> loginFailedLock;

    /**
     * 登录失败锁定阈值
     */
    private final ConfigRef<Integer> loginFailedLockThreshold;

    /**
     * 登录失败锁定时间 (分)
     */
    private final ConfigRef<Integer> loginFailedLockTime;

    /**
     * 登录失败发信
     */
    private final ConfigRef<Boolean> loginFailedSend;

    /**
     * 登录失败发送站内信阈值
     */
    private final ConfigRef<Integer> loginFailedSendThreshold;

    public AppLoginConfig(ConfigStore configStore) {
        this.loginSessionTime = configStore.int32(ConfigKeys.LOGIN_LOGIN_SESSION_TIME);
        this.allowMultiDevice = configStore.bool(ConfigKeys.LOGIN_ALLOW_MULTI_DEVICE);
        this.allowRefresh = configStore.bool(ConfigKeys.LOGIN_ALLOW_REFRESH);
        this.maxRefreshCount = configStore.int32(ConfigKeys.LOGIN_MAX_REFRESH_COUNT);
        this.refreshInterval = configStore.int32(ConfigKeys.LOGIN_REFRESH_INTERVAL);
        this.loginFailedLock = configStore.bool(ConfigKeys.LOGIN_LOGIN_FAILED_LOCK);
        this.loginFailedLockThreshold = configStore.int32(ConfigKeys.LOGIN_LOGIN_FAILED_LOCK_THRESHOLD);
        this.loginFailedLockTime = configStore.int32(ConfigKeys.LOGIN_LOGIN_FAILED_LOCK_TIME);
        this.loginFailedSend = configStore.bool(ConfigKeys.LOGIN_LOGIN_FAILED_SEND);
        this.loginFailedSendThreshold = configStore.int32(ConfigKeys.LOGIN_LOGIN_FAILED_SEND_THRESHOLD);
    }

    public Integer getLoginSessionTime() {
        return loginSessionTime.value;
    }

    public Boolean getAllowMultiDevice() {
        return allowMultiDevice.value;
    }

    public Boolean getAllowRefresh() {
        return allowRefresh.value;
    }

    public Integer getMaxRefreshCount() {
        return maxRefreshCount.value;
    }

    public Integer getRefreshInterval() {
        return refreshInterval.value;
    }

    public Boolean getLoginFailedLock() {
        return loginFailedLock.value;
    }

    public Integer getLoginFailedLockThreshold() {
        return loginFailedLockThreshold.value;
    }

    public Integer getLoginFailedLockTime() {
        return loginFailedLockTime.value;
    }

    public Boolean getLoginFailedSend() {
        return loginFailedSend.value;
    }

    public Integer getLoginFailedSendThreshold() {
        return loginFailedSendThreshold.value;
    }

}
