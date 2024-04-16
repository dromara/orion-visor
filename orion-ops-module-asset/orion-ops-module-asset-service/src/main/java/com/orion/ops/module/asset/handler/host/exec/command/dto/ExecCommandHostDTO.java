package com.orion.ops.module.asset.handler.host.exec.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量执行启动主机对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecCommandHostDTO", description = "批量执行启动主机对象")
public class ExecCommandHostDTO {

    @Schema(description = "hostLogId")
    private Long hostLogId;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "日志文件路径")
    private String logPath;

    @Schema(description = "脚本路径")
    private String scriptPath;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "超时时间")
    private Integer timeout;

}
