package com.orion.ops.module.asset.entity.dto;

import com.orion.ops.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机终端访问参数
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
@Schema(name = "HostTerminalAccessDTO", description = "主机终端访问参数")
public class HostTerminalAccessDTO {

    @Schema(description = "userId")
    private Long userId;

    @Schema(description = "username")
    private String username;

}
