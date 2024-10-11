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
package com.orion.visor.module.asset.entity.request.host;

import com.orion.visor.framework.common.entity.DataClearRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 主机连接日志 清理请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostConnectLogClearRequest", description = "主机连接日志 清理请求对象")
public class HostConnectLogClearRequest extends HostConnectLogQueryRequest implements DataClearRequest {

    @NotNull
    @Min(value = 1)
    @Max(value = 2000)
    @Schema(description = "清理数量限制")
    private Integer limit;

}
