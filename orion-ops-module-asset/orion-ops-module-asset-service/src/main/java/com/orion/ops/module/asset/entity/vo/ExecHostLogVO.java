package com.orion.ops.module.asset.entity.vo;

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

    @Schema(description = "执行状态")
    private String status;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "执行参数")
    private String parameter;

    @Schema(description = "退出码")
    private Integer exitStatus;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "执行开始时间")
    private Date startTime;

    @Schema(description = "执行结束时间")
    private Date finishTime;

}
