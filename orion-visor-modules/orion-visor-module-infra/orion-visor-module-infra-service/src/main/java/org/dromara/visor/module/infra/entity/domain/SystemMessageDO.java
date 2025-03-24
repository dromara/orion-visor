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
 * 系统消息 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_message", autoResultMap = true)
@Schema(name = "SystemMessageDO", description = "系统消息 实体对象")
public class SystemMessageDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息分类")
    @TableField("classify")
    private String classify;

    @Schema(description = "消息类型")
    @TableField("type")
    private String type;

    @Schema(description = "消息状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "消息关联")
    @TableField("rel_key")
    private String relKey;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "消息内容")
    @TableField("content")
    private String content;

    @Schema(description = "接收人id")
    @TableField("receiver_id")
    private Long receiverId;

    @Schema(description = "接收人用户名")
    @TableField("receiver_username")
    private String receiverUsername;

    @Schema(description = "创建人")
    @TableField(exist = false)
    private String creator;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

}
