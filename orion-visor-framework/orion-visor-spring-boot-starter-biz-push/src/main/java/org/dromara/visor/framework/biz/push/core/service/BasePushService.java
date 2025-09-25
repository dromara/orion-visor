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
package org.dromara.visor.framework.biz.push.core.service;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.entity.PushUser;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.push.core.message.PushMessage;

import java.util.stream.Collectors;

/**
 * 消息推送抽象服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 18:51
 */
@Slf4j
public abstract class BasePushService<Message extends PushMessage> implements IPushService<Message> {

    @Override
    public void push(Message message) {
        try {
            // 验证消息
            this.validateMessage(message);
        } catch (Exception e) {
            log.error("BasePushService validateMessage message: {}", JSON.toJSONString(message), e);
            return;
        }
        // 请求路径
        String url = this.buildRequestUrl(message);
        // 构建请求体
        String body = this.buildRequestBody(message);
        // 发送请求
        this.sendRequest(message, url, body);
    }

    /**
     * 验证消息
     *
     * @param message message
     */
    protected void validateMessage(Message message) {
        // 验证消息
        Assert.valid(message);
    }

    /**
     * 构建请求 url
     *
     * @param message message
     * @return url
     */
    protected abstract String buildRequestUrl(Message message);

    /**
     * 构建请求体
     *
     * @param message message
     * @return body
     */
    protected abstract String buildRequestBody(Message message);

    /**
     * 发送请求
     *
     * @param message message
     * @param url     url
     * @param body    body
     */
    protected abstract void sendRequest(Message message, String url, String body);

    /**
     * 追加 at 用户
     *
     * @param message message
     * @param content content
     * @return content
     */
    protected String appendAtUsers(Message message, String content) {
        if (Lists.isEmpty(message.getPushUsers())) {
            return content;
        }
        return content + Const.LF + this.getAtUsers(message);
    }

    /**
     * 获取 @ 的用户
     *
     * @param message message
     * @return users
     */
    protected String getAtUsers(Message message) {
        return Lists.stream(message.getPushUsers())
                .map(PushUser::getNickname)
                .filter(Strings::isNotBlank)
                .map(s -> Const.AT + s)
                .collect(Collectors.joining(Const.SPACE));
    }

}
