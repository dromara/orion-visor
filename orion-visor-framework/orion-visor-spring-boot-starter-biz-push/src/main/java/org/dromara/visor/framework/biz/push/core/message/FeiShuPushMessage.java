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
package org.dromara.visor.framework.biz.push.core.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;

import javax.validation.constraints.NotBlank;

/**
 * 飞书推送消息
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/16 00:26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FeiShuPushMessage", description = "飞书推送消息")
public class FeiShuPushMessage extends BasePushMessage {

    @NotBlank
    @Schema(description = "webhook")
    private String webhook;

    @Schema(description = "密钥")
    private String secret;

    @Override
    public PushChannelEnum getChannel() {
        return PushChannelEnum.FEI_SHU;
    }

}
