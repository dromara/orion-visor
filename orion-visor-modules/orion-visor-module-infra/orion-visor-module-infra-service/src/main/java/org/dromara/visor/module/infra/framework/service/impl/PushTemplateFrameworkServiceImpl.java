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

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.framework.service.PushTemplateFrameworkService;
import org.dromara.visor.framework.biz.push.core.message.PushMessage;
import org.dromara.visor.module.infra.entity.dto.NotifyTemplateDetailCacheDTO;
import org.dromara.visor.module.infra.service.NotifyTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 推送模板框架服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/21 16:42
 */
@Slf4j
@Service
public class PushTemplateFrameworkServiceImpl implements PushTemplateFrameworkService {

    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Override
    public PushMessage getPushMessageByTemplateId(Long templateId) {
        // 查询缓存
        NotifyTemplateDetailCacheDTO template = notifyTemplateService.getNotifyTemplateDetailByCache(templateId);
        boolean hasId = Optional.ofNullable(template)
                .map(NotifyTemplateDetailCacheDTO::getId)
                .isPresent();
        // 如果 id 不存在则证明模板不存在
        if (!hasId) {
            log.warn("PushTemplateFrameworkService template absent id: {}", templateId);
            return null;
        }
        // 根据渠道类型创建对应的推送消息
        PushChannelEnum channel = PushChannelEnum.of(template.getChannelType());
        if (channel == null) {
            return null;
        }
        // 创建消息
        PushMessage message = channel.createMessage(template.getChannelConfig());
        if (message == null) {
            log.warn("PushTemplateFrameworkService createMessage error id: {}", templateId);
            return null;
        }
        // 设置消息模板
        message.setTemplate(template.getMessageTemplate());
        return message;
    }

}
