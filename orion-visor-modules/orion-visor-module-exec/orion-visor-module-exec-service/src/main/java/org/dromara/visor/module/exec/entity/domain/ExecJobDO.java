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
package org.dromara.visor.module.exec.entity.domain;

import org.dromara.visor.framework.mybatis.core.domain.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 计划任务 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exec_job", autoResultMap = true)
@Schema(name = "ExecJobDO", description = "计划任务 实体对象")
public class ExecJobDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "任务名称")
    @TableField("name")
    private String name;

    @Schema(description = "执行序列")
    @TableField("exec_seq")
    private Integer execSeq;

    @Schema(description = "cron 表达式")
    @TableField("expression")
    private String expression;

    @Schema(description = "超时时间")
    @TableField("timeout")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    @TableField("script_exec")
    private Integer scriptExec;

    @Schema(description = "执行命令")
    @TableField("command")
    private String command;

    @Schema(description = "命令参数")
    @TableField("parameter_schema")
    private String parameterSchema;

    @Schema(description = "任务状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "最近执行id")
    @TableField("recent_log_id")
    private Long recentLogId;

    @Schema(description = "执行用户id")
    @TableField("exec_user_id")
    private Long execUserId;

    @Schema(description = "执行用户名")
    @TableField("exec_username")
    private String execUsername;

}
