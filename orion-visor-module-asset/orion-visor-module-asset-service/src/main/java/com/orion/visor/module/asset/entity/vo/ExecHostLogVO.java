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
package com.orion.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 批量执行主机日志 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecHostLogVO", description = "批量执行主机日志 视图响应对象")
public class ExecHostLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "执行日志id")
    private Long logId;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "主机名称")
    private String hostName;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "执行状态")
    private String status;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "执行参数")
    private String parameter;

    @Schema(description = "退出码")
    private Integer exitCode;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "执行开始时间")
    private Date startTime;

    @Schema(description = "执行结束时间")
    private Date finishTime;

}
