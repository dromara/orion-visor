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
import javax.validation.constraints.Size;

/**
 * 站内信推送
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/18 19:56
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "WebsiteMessage", description = "站内信消息")
public class WebsiteMessage extends BasePushMessage {

    @NotBlank
    @Size(max = 10)
    @Schema(description = "消息分类")
    private String messageClassify;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "消息类型")
    private String messageType;

    @Schema(description = "消息关联")
    private String relKey;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "标题")
    private String title;

    @Override
    public PushChannelEnum getChannel() {
        return PushChannelEnum.WEBSITE;
    }

}
