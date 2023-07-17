package com.orion.ops.module.infra.entity.request.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色菜单关联 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemRoleMenuCreateRequest", description = "角色菜单关联 创建请求对象")
public class SystemRoleMenuCreateRequest implements Serializable {

    @NotNull
    @Schema(description = "角色id")
    private Long roleId;

    @NotNull
    @Schema(description = "菜单id")
    private Long menuId;

}
