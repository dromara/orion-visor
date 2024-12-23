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
package org.dromara.visor.module.infra.entity.request.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 数据拓展信息 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataExtraQueryRequest", description = "数据拓展信息 查询请求对象")
public class DataExtraQueryRequest {

    @NonNull
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "数据id")
    private Long relId;

    @Schema(description = "数据id")
    private List<Long> relIdList;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "数据类型")
    private String type;

    @Size(max = 32)
    @Schema(description = "拓展项")
    private String item;

    @Schema(description = "拓展项")
    private List<String> items;

}
