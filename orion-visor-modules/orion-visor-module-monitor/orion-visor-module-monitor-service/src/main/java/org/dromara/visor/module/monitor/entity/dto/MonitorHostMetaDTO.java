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
package org.dromara.visor.module.monitor.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 监控元数据业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/13 23:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "MonitorHostMetaDTO", description = "监控元数据业务对象")
public class MonitorHostMetaDTO implements Serializable {

    @Schema(description = "cpu")
    private List<String> cpus;

    @Schema(description = "磁盘")
    private List<String> disks;

    @Schema(description = "网卡")
    private List<String> nets;

    @Schema(description = "内存大小")
    private Long memoryBytes;

}
