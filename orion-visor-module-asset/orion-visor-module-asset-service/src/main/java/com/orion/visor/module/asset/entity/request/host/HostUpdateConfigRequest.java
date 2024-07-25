package com.orion.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 主机 更新配置请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-13 14:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostUpdateConfigRequest", description = "主机 更新配置请求对象")
public class HostUpdateConfigRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Schema(description = "配置详情")
    private String config;

}
