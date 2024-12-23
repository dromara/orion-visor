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
package org.dromara.visor.module.infra.entity.request.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.common.entity.PageRequest;

import javax.validation.constraints.Size;

/**
 * 角色 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemRoleQueryRequest", description = "角色 查询请求对象")
public class SystemRoleQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "角色名称")
    private String name;

    @Size(max = 32)
    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "状态 0停用 1启用")
    private Integer status;

}
