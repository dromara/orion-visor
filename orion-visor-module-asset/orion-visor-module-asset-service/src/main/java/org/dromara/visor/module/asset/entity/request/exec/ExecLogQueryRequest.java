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
package org.dromara.visor.module.asset.entity.request.exec;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.common.entity.PageRequest;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 批量执行日志 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecLogQueryRequest", description = "批量执行日志 查询请求对象")
public class ExecLogQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "执行用户id")
    private Long userId;

    @Schema(description = "执行用户名")
    private String username;

    @Size(max = 12)
    @Schema(description = "执行来源")
    private String source;

    @Schema(description = "执行来源id")
    private Long sourceId;

    @Size(max = 8)
    @Schema(description = "执行方式")
    private String execMode;

    @Size(max = 128)
    @Schema(description = "执行描述")
    private String description;

    @Schema(description = "执行命令")
    private String command;

    @Size(max = 12)
    @Schema(description = "执行状态")
    private String status;

    @Schema(description = "开始时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] startTimeRange;

    @Schema(description = "创建时间 <=")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeLe;

    @Schema(description = "状态")
    private List<String> statusList;

}
