/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.desensitize.core.annotation.Desensitize;
import org.dromara.visor.framework.desensitize.core.annotation.DesensitizeObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量执行命令 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "ExecCommandRequest", description = "批量执行命令 请求对象")
public class ExecCommandRequest {

    @Size(max = 128)
    @Schema(description = "执行描述")
    private String description;

    @NonNull
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
    @Schema(description = "参数 schema")
    private String parameterSchema;

    @NotEmpty
    @Schema(description = "执行主机")
    private List<Long> hostIdList;

}
