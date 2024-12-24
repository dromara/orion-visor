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
 * 批量执行日志 实体对象
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
@TableName(value = "exec_log", autoResultMap = true)
@Schema(name = "ExecLogDO", description = "批量执行日志 实体对象")
public class ExecLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "执行用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "执行用户名")
    @TableField("username")
    private String username;

    @Schema(description = "执行来源")
    @TableField("source")
    private String source;

    @Schema(description = "执行来源id")
    @TableField("source_id")
    private Long sourceId;

    @Schema(description = "执行方式")
    @TableField("exec_mode")
    private String execMode;

    @Schema(description = "执行描述")
    @TableField("description")
    private String description;

    @Schema(description = "执行序列")
    @TableField("exec_seq")
    private Integer execSeq;

    @Schema(description = "执行命令")
    @TableField("command")
    private String command;

    @Schema(description = "参数 schema")
    @TableField("parameter_schema")
    private String parameterSchema;

    @Schema(description = "超时时间")
    @TableField("timeout")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    @TableField("script_exec")
    private Integer scriptExec;

    @Schema(description = "执行状态")
    @TableField("status")
    private String status;

    @Schema(description = "执行开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "执行完成时间")
    @TableField("finish_time")
    private Date finishTime;

}
