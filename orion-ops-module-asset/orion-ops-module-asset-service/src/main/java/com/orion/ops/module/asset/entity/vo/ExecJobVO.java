package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 计划执行任务 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecJobVO", description = "计划执行任务 视图响应对象")
public class ExecJobVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "执行序列")
    private Integer execSeq;

    @Schema(description = "cron 表达式")
    private String expression;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "命令参数")
    private String parameterSchema;

    @Schema(description = "启用状态 0禁用 1启用")
    private Integer status;

    @Schema(description = "最近执行id")
    private Long recentLogId;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "修改人")
    private String updater;

}
