package com.orion.visor.module.asset.handler.host.exec.command.dto;

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

    @Schema(description = "logId")
    private Long logId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "执行描述")
    private String description;

    @Schema(description = "执行序列")
    private Integer execSeq;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    private Boolean scriptExec;

    @Schema(description = "执行主机")
    private List<ExecCommandHostDTO> hosts;

}
