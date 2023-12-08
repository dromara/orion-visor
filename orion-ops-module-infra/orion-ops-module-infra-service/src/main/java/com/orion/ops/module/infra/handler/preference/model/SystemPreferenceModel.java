package com.orion.ops.module.infra.handler.preference.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统偏好模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPreferenceModel implements PreferenceModel {

    @Schema(description = "是否使用侧边菜单")
    private Boolean menu;

    @Schema(description = "是否使用顶部菜单")
    private Boolean topMenu;

    @Schema(description = "是否显示导航栏")
    private Boolean navbar;

    @Schema(description = "是否显示页脚")
    private Boolean footer;

    @Schema(description = "是否开启多页签")
    private Boolean tabBar;

    @Schema(description = "是否开启色弱模式")
    private Boolean colorWeak;

    @Schema(description = "菜单宽度")
    private Integer menuWidth;

    @Schema(description = "表格默认页数")
    private Integer defaultTablePageSize;

    @Schema(description = "卡片默认页数")
    private Integer defaultCardPageSize;

}
