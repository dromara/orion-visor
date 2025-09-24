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
package org.dromara.visor.framework.biz.push.core.utils;

import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.framework.biz.push.core.annotation.MessageChannel;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.message.PushMessage;
import org.dromara.visor.framework.biz.push.core.service.IPushService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 消息渠道工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 22:05
 */
public class MessageChannelUtils {

    private MessageChannelUtils() {
    }

    /**
     * 获取推送渠道
     *
     * @param service service
     * @return channel
     */
    public static PushChannelEnum getPushChannel(IPushService<? extends PushMessage> service) {
        // 获取类型注解
        MessageChannel messageChannel = AnnotationUtils.findAnnotation(service.getClass(), MessageChannel.class);
        if (messageChannel == null) {
            throw new BeanInitializationException(Strings.format("Push service [{}] not found @MessageChannel annotation", service.getClass().getName()));
        }
        return messageChannel.value();
    }

}
