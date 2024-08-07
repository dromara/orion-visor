package com.orion.visor.module.asset.entity.dto;

import com.orion.visor.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机终端传输参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 15:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "HostTerminalTransferDTO", description = "主机终端传输参数")
public class HostTerminalTransferDTO {

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "username")
    private String username;

}
