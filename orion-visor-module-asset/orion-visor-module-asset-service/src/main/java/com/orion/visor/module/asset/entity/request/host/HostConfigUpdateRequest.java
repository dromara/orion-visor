package com.orion.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 主机配置 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-13 14:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostConfigUpdateRequest", description = "主机配置 更新请求对象")
public class HostConfigUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "主机id")
    private Long hostId;

    @NotNull
    @Size(max = 32)
    @Schema(description = "配置类型")
    private String type;

    @NotBlank
    @Schema(description = "配置详情")
    private String config;

    @NotNull
    @Schema(description = "配置版本号")
    private Integer version;

}
