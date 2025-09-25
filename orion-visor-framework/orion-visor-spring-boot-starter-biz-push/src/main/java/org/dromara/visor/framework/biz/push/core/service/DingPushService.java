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
import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.lang.utils.crypto.Signatures;
import cn.orionsec.kit.lang.utils.math.Hex;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.framework.biz.push.core.annotation.MessageChannel;
import org.dromara.visor.framework.biz.push.core.entity.DingRequestBody;
import org.dromara.visor.framework.biz.push.core.entity.DingResponseBody;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.message.DingPushMessage;
import org.dromara.visor.framework.biz.push.core.utils.MessageUtils;

/**
 * 钉钉推送服务类
 * <p>
 * <a href="https://open.dingtalk.com/document/dingstart/obtain-the-webhook-address-of-a-custom-robot">doc</a>
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/17 23:13
 */
@Slf4j
@MessageChannel(value = PushChannelEnum.DING)
public class DingPushService extends BasePushService<DingPushMessage> {

    private static final String MARKDOWN = "markdown";

    private static final Integer SUCCESS_CODE = 0;

    @Override
    protected String buildRequestUrl(DingPushMessage message) {
        String secret = message.getSecret();
        if (Strings.isBlank(secret)) {
            return message.getWebhook();
        }
        // 加签
        try {
            long timestamp = System.currentTimeMillis();
            String plainText = timestamp + "\n" + secret;
            String hexSign = Signatures.hmacSha256(plainText, secret);
            if (hexSign == null) {
                log.error("DingPushService-sign error plain: {}", plainText);
                throw Exceptions.argument(ErrorMessage.GET_REQUEST_URL_ERROR);
            }
            byte[] signData = Hex.hexToBytes(hexSign);
            String sign = Base64s.encodeToString(signData);
            // 实际请求地址
            return message.getWebhook() + "&timestamp=" + timestamp + "&sign=" + sign;
        } catch (Exception e) {
            log.error("DingPushService-buildRequestUrl error", e);
            throw e;
        }
    }

    @Override
    protected String buildRequestBody(DingPushMessage message) {
        // 格式化内容
        String formattedContent = MessageUtils.format(message.getTemplate(), message.getParams());
        String formattedTitle = MessageUtils.format(message.getTitle(), message.getParams());
        // at 用户
        formattedContent = this.appendAtUsers(message, formattedContent);

        // 构建请求体
        return DingRequestBody.builder()
                .msgType(MARKDOWN)
                .markdown(DingRequestBody.MarkdownPayload.builder()
                        .title(formattedTitle)
                        .text(formattedContent)
                        .build())
                .build()
                .toJsonString();
    }

    @Override
    protected void sendRequest(DingPushMessage message, String url, String body) {
        // 发送请求
        OkResponse response = OkRequests.post(url, StandardContentType.APPLICATION_JSON_UTF8, body);
        DingResponseBody responseBody = JSON.parseObject(response.getBodyString(), DingResponseBody.class);
        // 验证发送结果
        Assert.eq(responseBody.getErrCode(), SUCCESS_CODE, responseBody.getErrMsg());
    }

}
