package com.orion.ops.module.asset.entity.request.exec;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 批量执行主机日志 查询请求对象
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
@Schema(name = "ExecHostLogQueryRequest", description = "批量执行主机日志 查询请求对象")
public class ExecHostLogQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "执行日志id")
    private Long logId;

    @Schema(description = "主机id")
    private Long hostId;

    @Size(max = 128)
    @Schema(description = "主机名称")
    private String hostName;

    @Size(max = 12)
    @Schema(description = "执行状态")
    private String status;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "执行参数")
    private String parameter;

    @Schema(description = "退出码")
    private Integer exitStatus;

    @Size(max = 512)
    @Schema(description = "日志路径")
    private String logPath;

    @Schema(description = "执行开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @Schema(description = "执行结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

}
