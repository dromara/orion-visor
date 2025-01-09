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
package org.dromara.visor.framework.web.core.deserializer;

import cn.orionsec.kit.lang.utils.Strings;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.RsaEncryptUtils;
import org.dromara.visor.common.utils.Valid;

import java.io.IOException;

/**
 * 参数解密反序列化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/5 15:32
 */
public class ParamDecryptDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String value = node.asText();
        // 为空直接返回
        if (Strings.isBlank(value)) {
            return value;
        }
        // 解密参数
        String decrypt = RsaEncryptUtils.decrypt(value);
        return Valid.notNull(decrypt, ErrorMessage.DECRYPT_ERROR);
    }

}