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
package org.dromara.visor.module.asset.entity.request.path;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 路径标签 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkCreateRequest", description = "路径标签 创建请求对象")
public class PathBookmarkCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组id")
    private Long groupId;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @NotBlank
    @Size(max = 4)
    @Schema(description = "类型")
    private String type;

    @NotBlank
    @Size(max = 1024)
    @Schema(description = "路径")
    private String path;

}
