package com.orion.ops.module.infra.handler.preference.model;

import com.alibaba.fastjson.JSON;
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

    @Schema(description = "新建连接类型")
    private String newConnectionType;

    @Schema(description = "终端主题")
    private JSONObject theme;

    @Schema(description = "显示设置")
    private JSONObject displaySetting;

    @Schema(description = "操作栏设置")
    private JSONObject actionBarSetting;

    @Schema(description = "背景设置")
    private JSONObject backgroundSetting;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplaySettingModel {

        @Schema(description = "字体样式")
        private String fontFamily;

        @Schema(description = "字体大小")
        private Integer fontSize;

        @Schema(description = "行高")
        private Double lineHeight;

        @Schema(description = "文本字重")
        private String fontWeight;

        @Schema(description = "加粗字重")
        private String fontWeightBold;

        @Schema(description = "光标样式")
        private String cursorStyle;

        @Schema(description = "光标闪烁")
        private Boolean cursorBlink;

        /**
         * 转为 json
         *
         * @return json
         */
        public JSONObject toJson() {
            return JSON.parseObject(JSON.toJSONString(this));
        }

    }

}
