package com.orion.ops.module.infra.entity.request.menu;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 菜单 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemMenuQueryRequest", description = "菜单 查询请求对象")
public class SystemMenuQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @Size(max = 32)
    @Schema(description = "菜单名称")
    private String name;

    @Size(max = 64)
    @Schema(description = "菜单权限")
    private String permission;

    @Schema(description = "菜单类型 1父菜单 2子菜单 3功能")
    private Integer type;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否可见 0不可见 1可见")
    private Integer visible;

    @Schema(description = "菜单状态 0停用 1启用")
    private Integer status;

    @Schema(description = "菜单缓存 0不缓存 1缓存")
    private Integer cache;

    @Size(max = 64)
    @Schema(description = "菜单图标")
    private String icon;

    @Size(max = 128)
    @Schema(description = "路由地址")
    private String path;

    @Size(max = 64)
    @Schema(description = "组件名称")
    private String componentName;

    @Size(max = 128)
    @Schema(description = "组件地址")
    private String component;

}
