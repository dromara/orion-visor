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
package org.dromara.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "OperatorLogVO", description = "操作日志 视图响应对象")
public class OperatorLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "traceId")
    private String traceId;

    @Schema(description = "请求ip")
    private String address;

    @Schema(description = "请求地址")
    private String location;

    @Schema(description = "userAgent")
    private String userAgent;

    @Schema(description = "风险等级")
    private String riskLevel;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "日志")
    private String logInfo;

    @Schema(description = "参数")
    private String extra;

    @Schema(description = "操作结果 0失败 1成功")
    private Integer result;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "返回值")
    private String returnValue;

    @Schema(description = "操作时间")
    private Integer duration;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "创建时间")
    private Date createTime;

}
