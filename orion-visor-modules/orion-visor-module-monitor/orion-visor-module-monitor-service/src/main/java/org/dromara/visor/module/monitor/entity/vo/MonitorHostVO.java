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
package org.dromara.visor.module.monitor.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.module.asset.entity.dto.host.HostAgentLogDTO;
import org.dromara.visor.module.infra.entity.dto.tag.TagDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostConfigDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostMetaDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 监控主机 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonitorHostVO", description = "监控主机 视图响应对象")
public class MonitorHostVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "策略id")
    private Long policyId;

    @Schema(description = "策略名称")
    private String policyName;

    @Schema(description = "系统类型")
    private String osType;

    @Schema(description = "主机名称")
    private String name;

    @Schema(description = "主机编码")
    private String code;

    @Schema(description = "主机地址")
    private String address;

    @Schema(description = "主机类型")
    private List<String> types;

    @Schema(description = "主机状态")
    private String status;

    @Schema(description = "agentKey")
    private String agentKey;

    @Schema(description = "探针版本")
    private String agentVersion;

    @Schema(description = "最新版本")
    private String latestVersion;

    @Schema(description = "探针安装状态")
    private Integer agentInstallStatus;

    @Schema(description = "探针在线状态")
    private Integer agentOnlineStatus;

    @Schema(description = "探针切换在线状态时间")
    private Date agentOnlineChangeTime;

    @Schema(description = "告警开关")
    private Integer alarmSwitch;

    @Schema(description = "负责人id")
    private Long ownerUserId;

    @Schema(description = "负责人用户名")
    private String ownerUsername;

    @Schema(description = "tags")
    private List<TagDTO> tags;

    @Schema(description = "监控元数据")
    private MonitorHostMetaDTO meta;

    @Schema(description = "监控配置")
    private MonitorHostConfigDTO config;

    @Schema(description = "监控数据")
    private MonitorHostMetricsDataVO metricsData;

    @Schema(description = "安装日志")
    private HostAgentLogDTO installLog;

}
