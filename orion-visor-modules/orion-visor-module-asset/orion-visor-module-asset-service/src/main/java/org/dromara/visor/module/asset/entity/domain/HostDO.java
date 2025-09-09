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

import java.util.Date;

/**
 * 主机 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "host", autoResultMap = true)
@Schema(name = "HostDO", description = "主机 实体对象")
public class HostDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "类型")
    @TableField("types")
    private String types;

    @Schema(description = "系统类型")
    @TableField("os_type")
    private String osType;

    @Schema(description = "系统架构")
    @TableField("arch_type")
    private String archType;

    @Schema(description = "主机名称")
    @TableField("name")
    private String name;

    @Schema(description = "主机编码")
    @TableField("code")
    private String code;

    @Schema(description = "主机地址")
    @TableField("address")
    private String address;

    @Schema(description = "主机状态")
    @TableField("status")
    private String status;

    @Schema(description = "agentKey")
    @TableField("agent_key")
    private String agentKey;

    @Schema(description = "探针版本")
    @TableField("agent_version")
    private String agentVersion;

    @Schema(description = "探针安装状态")
    @TableField("agent_install_status")
    private Integer agentInstallStatus;

    @Schema(description = "探针在线状态")
    @TableField("agent_online_status")
    private Integer agentOnlineStatus;

    @Schema(description = "探针切换在线状态时间")
    @TableField("agent_online_change_time")
    private Date agentOnlineChangeTime;

    @Schema(description = "主机描述")
    @TableField("description")
    private String description;

}
