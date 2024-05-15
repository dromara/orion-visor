package com.orion.visor.module.infra.entity.request.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜单 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMenuUpdateRequest", description = "菜单 更新请求对象")
public class SystemMenuUpdateRequest implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "菜单权限")
    private String permission;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "菜单类型 1父菜单 2子菜单 3功能")
    private Integer type;

    @Schema(description = "是否可见 0不可见 1可见")
    private Integer visible;

    @Schema(description = "菜单缓存 0不缓存 1缓存")
    private Integer cache;

    @Schema(description = "新窗口打开 0关闭 1开启")
    private Integer newWindow;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "链接地址")
    private String path;

    @Schema(description = "组件名称")
    private String component;

}
