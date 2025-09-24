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
package org.dromara.visor.module.infra.framework.service.impl;

import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.collect.Maps;
import org.dromara.visor.framework.biz.push.core.framework.service.WebsiteMessageFrameworkService;
import org.dromara.visor.framework.biz.push.core.message.WebsiteMessage;
import org.dromara.visor.module.infra.entity.request.message.SystemMessageCreateRequest;
import org.dromara.visor.module.infra.service.SystemMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 推送包 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 22:19
 */
@Service
public class WebsiteMessageFrameworkServiceImpl implements WebsiteMessageFrameworkService {

    @Resource
    private SystemMessageService systemMessageService;

    @Override
    public void push(WebsiteMessage message) {
        String relKey = Objects1.def(message.getRelKey(), Objects1.toString(Maps.get(message.getParams(), "relKey")));
        // 发送站内信
        message.getPushUsers()
                .stream()
                .map(s -> SystemMessageCreateRequest.builder()
                        .classify(message.getMessageClassify())
                        .type(message.getMessageType())
                        .relKey(relKey)
                        .title(message.getTitle())
                        .content(message.getTemplate())
                        .receiverId(s.getId())
                        .receiverUsername(s.getUsername())
                        .build())
                .forEach(systemMessageService::createSystemMessage);
    }

}
