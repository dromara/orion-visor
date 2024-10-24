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
package com.orion.visor.framework.common.entity;

import com.orion.visor.framework.common.validator.group.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 公共页码请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/12 23:14
 */
@Data
@Schema(description = "公共页码请求")
public class PageRequest {

    @NotNull(groups = Page.class)
    @Min(value = 1, groups = Page.class)
    @Max(value = 10000, groups = Page.class)
    @Schema(description = "页码")
    private Integer page;

    @NotNull(groups = Page.class)
    @Min(value = 1, groups = Page.class)
    @Max(value = 200, groups = Page.class)
    @Schema(description = "大小")
    private Integer limit;

}
