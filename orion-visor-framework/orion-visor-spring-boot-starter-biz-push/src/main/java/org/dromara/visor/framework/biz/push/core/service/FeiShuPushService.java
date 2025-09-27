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
import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.codec.Base64s;
import cn.orionsec.kit.lang.utils.crypto.Signatures;
import cn.orionsec.kit.lang.utils.crypto.enums.SecretKeySpecMode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.push.core.annotation.MessageChannel;
import org.dromara.visor.framework.biz.push.core.entity.FeiShuRequestBody;
import org.dromara.visor.framework.biz.push.core.entity.FeiShuResponseBody;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.message.FeiShuPushMessage;
import org.dromara.visor.framework.biz.push.core.utils.MessageUtils;

/**
 * 飞书推送服务类
 * <p>
 * <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot?lang=zh-CN">doc</a>
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/17 23:13
 */
@Slf4j
@MessageChannel(value = PushChannelEnum.FEI_SHU)
public class FeiShuPushService extends BasePushService<FeiShuPushMessage> {

    private static final String TEXT = "text";

    private static final Integer SUCCESS_CODE = 0;

    @Override
    protected String buildRequestUrl(FeiShuPushMessage message) {
        return message.getWebhook();
    }

    /**
     * 生成签名
     *
     * @param message   message
     * @param timestamp timestamp
     * @return sign
     */
    protected String buildSign(FeiShuPushMessage message, long timestamp) {
        String secret = message.getSecret();
        if (Strings.isBlank(secret)) {
            return null;
        }
        // 加签
        try {
            String stringToSign = timestamp + "\n" + secret;
            byte[] signData = Signatures.hmacHashSignBytes(new byte[]{}, Strings.bytes(stringToSign), SecretKeySpecMode.HMAC_SHA256);
            return Base64s.encodeToString(signData);
        } catch (Exception e) {
            log.error("FeiShuPushService-buildSign error", e);
            return null;
        }
    }

    @Override
    protected String buildRequestBody(FeiShuPushMessage message) {
        long timestamp = System.currentTimeMillis() / Const.MS_S_1;
        // 加签
        String sign = this.buildSign(message, timestamp);
        // 格式化内容
        String formattedContent = MessageUtils.format(message.getTemplate(), message.getParams());
        // at 的用户
        formattedContent = this.appendAtUsers(message, formattedContent);
        // 构建请求体
        return FeiShuRequestBody.builder()
                .timestamp(timestamp)
                .sign(sign)
                .msgType(TEXT)
                .content(FeiShuRequestBody.TextPayload.builder()
                        .text(formattedContent)
                        .build())
                .build()
                .toJsonString();
    }

    @Override
    protected void sendRequest(FeiShuPushMessage message, String url, String body) {
        // 发送请求
        OkResponse response = OkRequests.post(url, StandardContentType.APPLICATION_JSON_UTF8, body);
        FeiShuResponseBody responseBody = JSON.parseObject(response.getBodyString(), FeiShuResponseBody.class);
        // 验证发送结果
        Assert.eq(responseBody.getCode(), SUCCESS_CODE, responseBody.getMsg());
    }

}