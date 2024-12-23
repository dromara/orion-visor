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
package org.dromara.visor.module.infra.entity.request.exoression;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * cron 下次执行时间请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/2 16:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CronNextRequest", description = "cron 下次执行时间请求对象")
public class CronNextRequest implements Serializable {

    @NotBlank
    @Schema(description = "cron 表达式")
    private String expression;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Schema(description = "次数")
    private Integer times;

}
