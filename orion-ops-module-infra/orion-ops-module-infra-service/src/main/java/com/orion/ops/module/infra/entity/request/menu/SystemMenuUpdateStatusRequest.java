package com.orion.ops.module.infra.entity.request.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 菜单 更新状态对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMenuUpdateStatusRequest", description = "菜单 更新状态对象")
public class SystemMenuUpdateStatusRequest {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "菜单状态 0停用 1启用")
    private Integer status;

}
