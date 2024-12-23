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
package org.dromara.visor.module.infra.entity.request.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.common.entity.PageRequest;

import javax.validation.constraints.Size;

/**
 * 字典配置项 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-19 10:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DictKeyQueryRequest", description = "字典配置项 查询请求对象")
public class DictKeyQueryRequest extends PageRequest {

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "配置项")
    private String keyName;

    @Size(max = 64)
    @Schema(description = "配置描述")
    private String description;

}
