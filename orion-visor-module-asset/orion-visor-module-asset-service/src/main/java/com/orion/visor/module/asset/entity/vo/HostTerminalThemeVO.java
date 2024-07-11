package com.orion.visor.module.asset.entity.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机终端主题 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/4 19:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostTerminalThemeVO", description = "主机终端主题 视图响应对象")
public class HostTerminalThemeVO {

    @Schema(description = "主题名称")
    private String name;

    @Schema(description = "是否为暗色")
    private Boolean dark;

    @Schema(description = "主题 schema")
    private JSONObject schema;

}
