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
package org.dromara.visor.framework.biz.push.core.entity;

import cn.orionsec.kit.lang.able.IJsonObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 钉钉请求体
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 18:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingRequestBody implements Serializable, IJsonObject {

    /**
     * 消息类型
     */
    @JSONField(name = "msgtype")
    private String msgType;

    /**
     * markdown 内容
     */
    private DingRequestBody.MarkdownPayload markdown;

    /**
     * at 配置
     */
    private DingRequestBody.AtPayload at;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarkdownPayload implements Serializable {

        /**
         * 标题
         */
        private String title;

        /**
         * 内容
         */
        private String text;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AtPayload implements Serializable {

        /**
         * 被 at 的手机号
         */
        private List<String> atMobiles;

        /**
         * 被 at 的 userId
         */
        private List<String> atUserIds;

        /**
         * 是否 at 所有人
         */
        private Boolean isAtAll;
    }

}
