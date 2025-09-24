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

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.spring.SpringHolder;
import org.dromara.visor.common.entity.PushUser;
import org.dromara.visor.framework.biz.push.core.event.PushMessageEvent;
import org.dromara.visor.framework.biz.push.core.framework.service.PushTemplateFrameworkService;
import org.dromara.visor.framework.biz.push.core.message.PushMessage;

import java.util.List;
import java.util.Map;

/**
 * 消息推送工具类
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/17 23:13
 */
public class PushUtils {

    private static PushTemplateFrameworkService pushTemplateFrameworkService;

    /**
     * 发布消息推送事件
     *
     * @param pushMessage 推送消息配置
     */
    public static void push(PushMessage pushMessage) {
        SpringHolder.publishEvent(new PushMessageEvent(pushMessage));
    }

    /**
     * 根据模板推送消息
     *
     * @param templateId 模板ID
     * @param params     模板参数
     */
    public static void pushTemplate(Long templateId, Map<String, Object> params) {
        pushTemplate(templateId, params, null);
    }

    /**
     * 根据模板推送消息
     *
     * @param templateId 模板ID
     * @param params     模板参数
     * @param pushUsers  推送用户
     */
    public static void pushTemplate(Long templateId, Map<String, Object> params, List<PushUser> pushUsers) {
        PushMessage pushMessage = pushTemplateFrameworkService.getPushMessageByTemplateId(templateId);
        if (pushMessage == null) {
            return;
        }
        // 设置模板参数
        pushMessage.setParams(params);
        pushMessage.setPushUsers(pushUsers);
        // 发布消息
        push(pushMessage);
    }

    public static void setPushTemplateFrameworkService(PushTemplateFrameworkService pushTemplateFrameworkService) {
        if (PushUtils.pushTemplateFrameworkService != null) {
            // unmodified
            throw Exceptions.state();
        }
        PushUtils.pushTemplateFrameworkService = pushTemplateFrameworkService;
    }

}
