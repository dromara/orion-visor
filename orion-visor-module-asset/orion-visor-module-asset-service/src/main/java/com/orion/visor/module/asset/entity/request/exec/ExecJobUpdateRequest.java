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
package com.orion.visor.module.asset.entity.request.exec;

import com.orion.visor.framework.desensitize.core.annotation.Desensitize;
import com.orion.visor.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 计划任务 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "ExecJobUpdateRequest", description = "计划任务 更新请求对象")
public class ExecJobUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "任务名称")
    private String name;

    @NotBlank
    @Size(max = 512)
    @Schema(description = "cron 表达式")
    private String expression;

    @NotNull
    @Schema(description = "超时时间")
    private Integer timeout;

    @NonNull
    @Schema(description = "是否使用脚本执行")
    private Integer scriptExec;

    @NotBlank
    @Desensitize(toEmpty = true)
    @Schema(description = "执行命令")
    private String command;

    @NotBlank
    @Schema(description = "命令参数")
    private String parameterSchema;

    @NotEmpty
    @Schema(description = "执行主机")
    private List<Long> hostIdList;

}
