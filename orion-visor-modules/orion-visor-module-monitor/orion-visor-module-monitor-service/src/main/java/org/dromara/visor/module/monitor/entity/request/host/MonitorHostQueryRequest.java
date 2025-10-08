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
package org.dromara.visor.module.monitor.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.visor.common.entity.BaseQueryRequest;
import org.dromara.visor.common.validator.group.Key;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 监控主机 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "MonitorHostQueryRequest", description = "监控主机 查询请求对象")
public class MonitorHostQueryRequest extends BaseQueryRequest {

    @NotEmpty(groups = Key.class)
    @Schema(description = "agentKey")
    private List<String> agentKeys;

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "告警开关")
    private Integer alarmSwitch;

    @Schema(description = "策略id")
    private Long policyId;

    @Schema(description = "负责人id")
    private Long ownerUserId;

    @Size(max = 64)
    @Schema(description = "主机名称")
    private String name;

    @Size(max = 64)
    @Schema(description = "主机编码")
    private String code;

    @Size(max = 128)
    @Schema(description = "主机地址")
    private String address;

    @Schema(description = "探针安装状态")
    private Integer agentInstallStatus;

    @Schema(description = "探针在线状态")
    private Integer agentOnlineStatus;

    @Size(max = 255)
    @Schema(description = "描述")
    private String description;

    @Schema(description = "tag")
    private List<Long> tags;

    @Schema(description = "是否查询分组信息")
    private Boolean queryGroup;

    @Schema(description = "是否查询 tag 信息")
    private Boolean queryTag;

    @Schema(description = "是否查询规格信息")
    private Boolean querySpec;

}
