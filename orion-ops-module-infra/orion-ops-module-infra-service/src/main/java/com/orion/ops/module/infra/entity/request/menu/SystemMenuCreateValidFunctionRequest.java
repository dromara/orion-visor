package com.orion.ops.module.infra.entity.request.menu;

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
 * 菜单 创建功能请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMenuCreateFunctionRequest", description = "菜单 创建功能请求对象")
public class SystemMenuCreateValidFunctionRequest implements Serializable {

    @NotNull
    @Schema(description = "父id")
    private Long parentId;

    @Size(max = 32)
    @NotBlank
    @Schema(description = "菜单名称")
    private String name;

    @Size(max = 64)
    @NotBlank
    @Schema(description = "菜单权限")
    private String permission;

    @NotNull
    @Schema(description = "菜单类型 1父菜单 2子菜单 3功能")
    private Integer type;

    @NotNull
    @Schema(description = "菜单状态 0停用 1启用")
    private Integer status;

}
