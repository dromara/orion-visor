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

/**
 * 飞书请求体
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 18:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeiShuRequestBody implements Serializable, IJsonObject {

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 签名
     */
    private String sign;

    /**
     * 消息类型
     */
    @JSONField(name = "msg_type")
    private String msgType;

    /**
     * text 内容
     */
    private TextPayload content;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextPayload implements Serializable {

        /**
         * 内容
         */
        private String text;

    }

}
