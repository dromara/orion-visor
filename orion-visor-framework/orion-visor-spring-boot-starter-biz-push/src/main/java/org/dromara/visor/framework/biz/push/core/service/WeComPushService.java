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

import cn.orionsec.kit.http.ok.OkRequests;
import cn.orionsec.kit.http.ok.OkResponse;
import cn.orionsec.kit.lang.constant.StandardContentType;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.push.core.annotation.MessageChannel;
import org.dromara.visor.framework.biz.push.core.entity.WeComRequestBody;
import org.dromara.visor.framework.biz.push.core.entity.WeComResponseBody;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.message.WeComPushMessage;
import org.dromara.visor.framework.biz.push.core.utils.MessageUtils;

/**
 * 企业微信推送服务
 * <p>
 * <a href="https://developer.work.weixin.qq.com/document/path/99110">docs</a>
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/17 23:13
 */
@Slf4j
@MessageChannel(value = PushChannelEnum.WE_COM)
public class WeComPushService extends BasePushService<WeComPushMessage> {

    private static final String MAKRDOWN = "markdown";

    private static final Integer SUCCESS_CODE = 0;

    @Override
    protected String buildRequestUrl(WeComPushMessage message) {
        return message.getWebhook();
    }

    @Override
    protected String buildRequestBody(WeComPushMessage message) {
        // 格式化内容
        String formattedContent = MessageUtils.format(message.getTemplate(), message.getParams());
        // at 用户
        formattedContent = this.appendAtUsers(message, formattedContent);
        // 构建请求体
        WeComRequestBody.WeComRequestBodyBuilder builder = WeComRequestBody.builder()
                .msgType(MAKRDOWN)
                .markdown(WeComRequestBody.MarkdownPayload.builder()
                        .content(formattedContent)
                        .build());

        return builder.build().toJsonString();
    }

    @Override
    protected void sendRequest(WeComPushMessage message, String url, String body) {
        // 发送请求
        OkResponse response = OkRequests.post(url, StandardContentType.APPLICATION_JSON_UTF8, body);
        WeComResponseBody responseBody = JSON.parseObject(response.getBodyString(), WeComResponseBody.class);
        // 验证发送结果
        Valid.eq(responseBody.getErrCode(), SUCCESS_CODE, responseBody.getErrMsg());
    }

}