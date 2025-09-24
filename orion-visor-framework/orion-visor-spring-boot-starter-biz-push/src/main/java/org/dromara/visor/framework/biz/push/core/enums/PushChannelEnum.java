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
package org.dromara.visor.framework.biz.push.core.enums;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.visor.framework.biz.push.core.message.*;

/**
 * 通知模板类型枚举
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/15 23:20
 */
@Getter
@AllArgsConstructor
public enum PushChannelEnum {

    /**
     * 站内信
     */
    WEBSITE(WebsiteMessage.class),

    /**
     * 钉钉
     */
    DING(DingPushMessage.class),

    /**
     * 飞书
     */
    FEI_SHU(FeiShuPushMessage.class),

    /**
     * 企业微信
     */
    WE_COM(WeComPushMessage.class),

    ;

    public final Class<?> messageClass;

    @SuppressWarnings("unchecked")
    public <T extends PushMessage> T createMessage(String config) {
        try {
            return (T) JSON.parseObject(config, messageClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据渠道名称获取枚举
     *
     * @param channel 渠道名称
     * @return 枚举
     */
    public static PushChannelEnum of(String channel) {
        for (PushChannelEnum value : values()) {
            if (value.name().equalsIgnoreCase(channel)) {
                return value;
            }
        }
        return WEBSITE;
    }

}