package com.orion.ops.module.infra.handler.preference.model;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端偏好模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/8 14:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalPreferenceModel implements PreferenceModel {

    @Schema(description = "暗色主题")
    private String darkTheme;

    @Schema(description = "终端主题")
    private JSONObject terminalTheme;

    @Schema(description = "显示设置")
    private JSONObject viewSetting;

}
