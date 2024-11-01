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
package org.dromara.visor.module.asset.entity.request.host;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.common.entity.PageRequest;
import org.dromara.visor.framework.common.validator.group.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 终端连接日志 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TerminalConnectLogQueryRequest", description = "终端连接日志 查询请求对象")
public class TerminalConnectLogQueryRequest extends PageRequest {

    @NotNull(groups = Id.class)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "id")
    private List<Long> idList;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "主机id")
    private Long hostId;

    @Size(max = 128)
    @Schema(description = "主机地址")
    private String hostAddress;

    @Size(max = 16)
    @Schema(description = "类型")
    private String type;

    @Size(max = 128)
    @Schema(description = "token")
    private String token;

    @Size(max = 16)
    @Schema(description = "状态")
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
