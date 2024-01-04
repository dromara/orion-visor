package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主机终端访问 响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/4 15:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostTerminalAccessVO", description = "主机终端访问 响应对象")
public class HostTerminalAccessVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "accessToken")
    private String accessToken;

    @Schema(description = "session 起始量")
    private String sessionInitial;

}
