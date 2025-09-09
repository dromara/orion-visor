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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 监控主机图表 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "MonitorHostChartRequest", description = "监控主机图表 查询请求对象")
public class MonitorHostChartRequest extends BaseQueryRequest {

    @NotEmpty
    @Schema(description = "agentKey")
    private List<String> agentKeys;

    @NotBlank
    @Schema(description = "表")
    private String measurement;

    @NotEmpty
    @Schema(description = "字段")
    private List<String> fields;

    @NotBlank
    @Schema(description = "时间窗口")
    private String window;

    @NotBlank
    @Schema(description = "聚合参数")
    private String aggregate;

    @Schema(description = "聚合参数")
    private Long start;

    @Schema(description = "聚合参数")
    private Long end;

    @Schema(description = "区间")
    private String range;

}
