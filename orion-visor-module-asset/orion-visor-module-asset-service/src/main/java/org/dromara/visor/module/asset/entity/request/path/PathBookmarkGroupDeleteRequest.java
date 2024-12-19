/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
import org.dromara.visor.framework.common.validator.group.Id;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 路径标签分组 删除请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkGroupDeleteRequest", description = "路径标签分组 删除请求对象")
public class PathBookmarkGroupDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Id.class)
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "是否删除组内数据")
    private Boolean deleteItem;

}
