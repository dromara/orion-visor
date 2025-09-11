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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 监控主机配置更新请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/13 23:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonitorHostAgentConfigUpdateRequest", description = "监控主机配置更新请求")
public class MonitorHostAgentConfigUpdateRequest implements Serializable {

    @Schema(description = "cpu索引名称")
    private String cpuName;

    @Schema(description = "磁盘名称")
    private String diskName;

    @Schema(description = "网卡名称")
    private String networkName;

}
