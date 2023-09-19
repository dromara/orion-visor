package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class HostConfigUpdateStatusRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "状态 0停用 1启用")
    private Integer status;

    @NotNull
    @Schema(description = "配置版本号")
    private Integer version;

}
