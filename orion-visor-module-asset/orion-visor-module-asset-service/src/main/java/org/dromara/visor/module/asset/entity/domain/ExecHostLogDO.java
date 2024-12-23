/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.util.Date;

/**
 * 批量执行主机日志 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exec_host_log", autoResultMap = true)
@Schema(name = "ExecHostLogDO", description = "批量执行主机日志 实体对象")
public class ExecHostLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "执行日志id")
    @TableField("log_id")
    private Long logId;

    @Schema(description = "主机id")
    @TableField("host_id")
    private Long hostId;

    @Schema(description = "主机名称")
    @TableField("host_name")
    private String hostName;

    @Schema(description = "主机地址")
    @TableField("host_address")
    private String hostAddress;

    @Schema(description = "执行状态")
    @TableField("status")
    private String status;

    @Schema(description = "执行命令")
    @TableField("command")
    private String command;

    @Schema(description = "执行参数")
    @TableField("parameter")
    private String parameter;

    @Schema(description = "退出码")
    @TableField("exit_code")
    private Integer exitCode;

    @Schema(description = "日志路径")
    @TableField("log_path")
    private String logPath;

    @Schema(description = "脚本路径")
    @TableField("script_path")
    private String scriptPath;

    @Schema(description = "错误信息")
    @TableField("error_message")
    private String errorMessage;

    @Schema(description = "执行开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "执行结束时间")
    @TableField("finish_time")
    private Date finishTime;

}
