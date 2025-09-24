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
package org.dromara.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

/**
 * 通知模板 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "notify_template", autoResultMap = true)
@Schema(name = "NotifyTemplateDO", description = "通知模板 实体对象")
public class NotifyTemplateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "通知名称")
    @TableField("name")
    private String name;

    @Schema(description = "业务类型")
    @TableField("biz_type")
    private String bizType;

    @Schema(description = "渠道类型")
    @TableField("channel_type")
    private String channelType;

    @Schema(description = "渠道配置")
    @TableField("channel_config")
    private String channelConfig;

    @Schema(description = "消息模板")
    @TableField("message_template")
    private String messageTemplate;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

}
