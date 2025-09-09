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

/**
 * 监控主机指标数据
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/15 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonitorHostMetricsDataVO", description = "监控主机指标数据 视图响应对象")
public class MonitorHostMetricsDataVO {

    @Schema(description = "agentKey")
    private String agentKey;

    @Schema(description = "是否无数据")
    private Boolean noData;

    @Schema(description = "采集时间")
    private Long timestamp;

    @Schema(description = "cpu名称")
    private String cpuName;

    @Schema(description = "磁盘名称")
    private String diskName;

    @Schema(description = "网卡名称")
    private String networkName;

    @Schema(description = "cpu 使用率")
    private Double cpuUsagePercent;

    @Schema(description = "内存使用率")
    private Double memoryUsagePercent;

    @Schema(description = "内存使用量")
    private Long memoryUsageBytes;

    @Schema(description = "load1")
    private Double load1;

    @Schema(description = "load5")
    private Double load5;

    @Schema(description = "load15")
    private Double load15;

    @Schema(description = "磁盘使用率")
    private Double diskUsagePercent;

    @Schema(description = "磁盘使用量")
    private Long diskUsageBytes;

    @Schema(description = "网卡上行带宽速度")
    private Double networkSentPreBytes;

    @Schema(description = "网卡下行带宽速度")
    private Double networkRecvPreBytes;

    public static MonitorHostMetricsDataVO noData(String agentKey) {
        return MonitorHostMetricsDataVO.builder()
                .noData(true)
                .agentKey(agentKey)
                .build();
    }

}
