package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Schema(description = "hostId")
    private Long hostId;

    @NotNull
    @Schema(description = "配置类型")
    private String type;

    @NotBlank
    @Schema(description = "配置详情")
    private String config;

    @NotNull
    @Schema(description = "配置版本号")
    private Integer version;

}
