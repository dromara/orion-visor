package com.orion.ops.module.infra.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:25
 */
@Data
@Schema(name = "SystemMenuCacheDTO", description = "菜单 缓存业务对象")
public class SystemMenuCacheDTO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @Schema(description = "菜单名称")
    private String name;

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

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件地址")
    private String component;

}
