/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 执行主机日志查看 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 16:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecHostLogTailDTO", description = "执行主机日志查看 缓存对象")
public class ExecHostLogTailDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "输出编码")
    private String charset;

}
