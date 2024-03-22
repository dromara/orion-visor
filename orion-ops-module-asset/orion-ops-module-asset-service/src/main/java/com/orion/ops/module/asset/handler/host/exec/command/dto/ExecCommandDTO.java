package com.orion.ops.module.asset.handler.host.exec.command.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量执行启动对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecCommandDTO", description = "批量执行启动对象")
public class ExecCommandDTO {

    @Schema(description = "hostId")
    private Long logId;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "主机")
    private List<ExecCommandHostDTO> hosts;

}
