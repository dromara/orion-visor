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
package org.dromara.visor.module.monitor.entity.domain;

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
 * 监控主机 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "monitor_host", autoResultMap = true)
@Schema(name = "MonitorHostDO", description = "监控主机 实体对象")
public class MonitorHostDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主机id")
    @TableField("host_id")
    private Long hostId;

    @Schema(description = "策略id")
    @TableField("policy_id")
    private Long policyId;

    @Schema(description = "agentKey")
    @TableField("agent_key")
    private String agentKey;

    @Schema(description = "告警开关")
    @TableField("alarm_switch")
    private Integer alarmSwitch;

    @Schema(description = "负责人id")
    @TableField("owner_user_id")
    private Long ownerUserId;

    @Schema(description = "负责人用户名")
    @TableField("owner_username")
    private String ownerUsername;

    @Schema(description = "监控元数据")
    @TableField("monitor_meta")
    private String monitorMeta;

    @Schema(description = "监控配置")
    @TableField("monitor_config")
    private String monitorConfig;

}
