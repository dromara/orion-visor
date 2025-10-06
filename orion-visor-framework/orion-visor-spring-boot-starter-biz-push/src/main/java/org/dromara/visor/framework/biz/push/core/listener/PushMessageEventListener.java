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
package org.dromara.visor.framework.biz.push.core.listener;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.event.PushMessageEvent;
import org.dromara.visor.framework.biz.push.core.message.PushMessage;
import org.dromara.visor.framework.biz.push.core.service.IPushService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 消息推送事件监听器
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/1/15
 */
@Slf4j
public class PushMessageEventListener implements ApplicationListener<PushMessageEvent> {

    private final Map<PushChannelEnum, IPushService<? extends PushMessage>> pushServiceMap;

    public PushMessageEventListener(Map<PushChannelEnum, IPushService<? extends PushMessage>> pushServiceMap) {
        this.pushServiceMap = pushServiceMap;
    }

    @Async("pushExecutor")
    @Override
    public void onApplicationEvent(PushMessageEvent event) {
        try {
            // 获取消息
            PushMessage message = (PushMessage) event.getSource();
            // 发送消息
            this.getPushService(message).push(message);
            log.info("PushMessageEventListener push success");
        } catch (Exception e) {
            log.error("PushMessageEventListener push error", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends PushMessage> IPushService<T> getPushService(PushMessage message) {
        return (IPushService<T>) pushServiceMap.get(message.getChannel());
    }

}
