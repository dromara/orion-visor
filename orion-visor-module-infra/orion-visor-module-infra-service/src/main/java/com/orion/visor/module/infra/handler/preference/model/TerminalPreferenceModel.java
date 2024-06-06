package com.orion.visor.module.infra.handler.preference.model;

import com.alibaba.fastjson.JSONObject;
import com.orion.lang.able.IJsonObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Schema(description = "右键菜单设置")
    private List<String> rightMenuSetting;

    @Schema(description = "交互设置")
    private JSONObject interactSetting;

    @Schema(description = "插件设置")
    private JSONObject pluginsSetting;

    @Schema(description = "会话设置")
    private JSONObject sessionSetting;

    @Schema(description = "快捷键设置")
    private JSONObject shortcutSetting;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplaySettingModel implements IJsonObject {

        @Schema(description = "字体样式")
        private String fontFamily;

        @Schema(description = "字体大小")
        private Integer fontSize;

        @Schema(description = "行高")
        private Double lineHeight;

        @Schema(description = "字间距")
        private Integer letterSpacing;

        @Schema(description = "文本字重")
        private String fontWeight;

        @Schema(description = "加粗字重")
        private String fontWeightBold;

        @Schema(description = "光标样式")
        private String cursorStyle;

        @Schema(description = "光标闪烁")
        private Boolean cursorBlink;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InteractSettingModel implements IJsonObject {

        @Schema(description = "快速滚动")
        private Boolean fastScrollModifier;

        @Schema(description = "点击移动光标")
        private Boolean altClickMovesCursor;

        @Schema(description = "右键选中词条")
        private Boolean rightClickSelectsWord;

        @Schema(description = "选中自动复制")
        private Boolean selectionChangeCopy;

        @Schema(description = "复制去除空格")
        private Boolean copyAutoTrim;

        @Schema(description = "粘贴去除空格")
        private Boolean pasteAutoTrim;

        @Schema(description = "右键粘贴")
        private Boolean rightClickPaste;

        @Schema(description = "启用右键菜单")
        private Boolean enableRightClickMenu;

        @Schema(description = "启用响铃")
        private Boolean enableBell;

        @Schema(description = "单词分隔符")
        private String wordSeparator;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PluginsSettingModel implements IJsonObject {

        @Schema(description = "超链接插件")
        private Boolean enableWeblinkPlugin;

        @Schema(description = "WebGL 渲染插件")
        private Boolean enableWebglPlugin;

        @Schema(description = "unicode11 插件")
        private Boolean enableUnicodePlugin;

        @Schema(description = "图片渲染插件")
        private Boolean enableImagePlugin;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionSettingModel implements IJsonObject {

        @Schema(description = "伪终端类型")
        private String terminalEmulationType;

        @Schema(description = "保存在缓冲区的行数")
        private Integer scrollBackLine;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortcutSettingModel implements IJsonObject {

        @Schema(description = "是否启用")
        private Boolean enabled;

        @Schema(description = "快捷键定义")
        private List<ShortcutKeysModel> keys;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionBarSettingModel implements IJsonObject {

        @Schema(description = "命令输入框")
        private Boolean commandInput;

        @Schema(description = "连接状态")
        private Boolean connectStatus;

        @Schema(description = "去顶部")
        private Boolean toTop;

        @Schema(description = "去底部")
        private Boolean toBottom;

        @Schema(description = "全选")
        private Boolean selectAll;

        @Schema(description = "搜索")
        private Boolean search;

        @Schema(description = "复制")
        private Boolean copy;

        @Schema(description = "粘贴")
        private Boolean paste;

        @Schema(description = "中断")
        private Boolean interrupt;

        @Schema(description = "回车")
        private Boolean enter;

        @Schema(description = "增大字号")
        private Boolean fontSizePlus;

        @Schema(description = "减小字号")
        private Boolean fontSizeSubtract;

        @Schema(description = "命令编辑器")
        private Boolean commandEditor;

        @Schema(description = "打开 SFTP")
        private Boolean openSftp;

        @Schema(description = "清空")
        private Boolean clear;

        @Schema(description = "断开连接")
        private Boolean disconnect;

        @Schema(description = "关闭终端")
        private Boolean closeTab;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortcutKeysModel implements IJsonObject {

        @Schema(description = "快捷键操作")
        private String item;

        @Schema(description = "ctrl 按键")
        private Boolean ctrlKey;

        @Schema(description = "shift 按键")
        private Boolean shiftKey;

        @Schema(description = "alt 按键")
        private Boolean altKey;

        @Schema(description = "实际按键")
        private String code;

        @Schema(description = "是否启用")
        private Boolean enabled;

    }

}
