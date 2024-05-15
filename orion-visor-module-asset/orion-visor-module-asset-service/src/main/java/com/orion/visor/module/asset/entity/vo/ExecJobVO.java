package com.orion.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 计划任务 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecJobVO", description = "计划任务 视图响应对象")
public class ExecJobVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "cron 表达式")
    private String expression;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    private Integer scriptExec;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "命令参数")
    private String parameterSchema;

    @Schema(description = "任务状态")
    private Integer status;

    @Schema(description = "最近执行id")
    private Long recentLogId;

    @Schema(description = "最近执行状态")
    private String recentLogStatus;

    @Schema(description = "最近执行时间")
    private Date recentLogTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "执行主机")
    private List<Long> hostIdList;

    @Schema(description = "执行主机")
    private List<HostVO> hostList;

}
