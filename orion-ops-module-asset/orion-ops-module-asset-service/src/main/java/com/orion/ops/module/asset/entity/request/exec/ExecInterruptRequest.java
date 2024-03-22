package com.orion.ops.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中断执行命令
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 18:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecInterruptRequest", description = "中断执行命令 请求对象")
public class ExecInterruptRequest {

    @Schema(description = "执行日志id")
    private Long logId;

    @Schema(description = "执行主机日志id")
    private Long hostLogId;

}
