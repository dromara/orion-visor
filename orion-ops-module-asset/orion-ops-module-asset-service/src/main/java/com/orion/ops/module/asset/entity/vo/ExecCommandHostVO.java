package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 命令执行主机 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 14:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecCommandHostVO", description = "命令执行主机 视图响应对象")
public class ExecCommandHostVO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "主机名称")
    private String hostName;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "执行状态")
    private String status;

}
