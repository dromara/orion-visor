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
package org.dromara.visor.module.asset.entity.domain;

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
 * 主机配置 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-3-6 10:59
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "host_config", autoResultMap = true)
@Schema(name = "HostConfigDO", description = "主机配置 实体对象")
public class HostConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主机id")
    @TableField("host_id")
    private Long hostId;

    @Schema(description = "配置类型")
    @TableField("type")
    private String type;

    @Schema(description = "配置状态")
    @TableField("status")
    private String status;

    @Schema(description = "配置值")
    @TableField("config")
    private String config;

}
