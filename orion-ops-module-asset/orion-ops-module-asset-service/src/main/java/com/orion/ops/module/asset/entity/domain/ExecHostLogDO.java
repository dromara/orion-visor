package com.orion.ops.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
    @TableField("exit_status")
    private Integer exitStatus;

    @Schema(description = "日志路径")
    @TableField("log_path")
    private String logPath;

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
