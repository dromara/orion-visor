package com.orion.ops.module.infra.handler.preference.strategy;

import com.alibaba.fastjson.JSONObject;
import com.orion.net.host.ssh.TerminalType;
import com.orion.ops.module.infra.handler.preference.model.TerminalPreferenceModel;
import org.springframework.stereotype.Component;

/**
 * 终端偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/8 14:46
 */
@Component
public class TerminalPreferenceStrategy implements IPreferenceStrategy<TerminalPreferenceModel> {

    @Override
    public TerminalPreferenceModel getDefault() {
        // ...快捷键 ...背景
        // 默认显示设置
        String defaultDisplaySetting = TerminalPreferenceModel.DisplaySettingModel
                .builder()
                .fontFamily("_")
                .fontSize(14)
                .lineHeight(1.00)
                .fontWeight("normal")
                .fontWeightBold("bold")
                .cursorStyle("bar")
                .cursorBlink(true)
                .build()
                .toJsonString();
        // 默认交互设置
        String defaultInteractSetting = TerminalPreferenceModel.InteractSettingModel.builder()
                .fastScrollModifier(true)
                .altClickMovesCursor(true)
                .rightClickSelectsWord(false)
                .selectionChangeCopy(false)
                .copyAutoTrim(false)
                .pasteAutoTrim(false)
                .rightClickPaste(false)
                .enableRightClickMenu(true)
                .enableBell(false)
                .wordSeparator("/\\()\"'` -.,:;<>~!@#$%^&*|+=[]{}~?│")
                .build()
                .toJsonString();
        // 默认插件设置
        String defaultPluginsSetting = TerminalPreferenceModel.PluginsSettingModel.builder()
                .enableWeblinkPlugin(true)
                .enableWebglPlugin(true)
                .enableImagePlugin(false)
                .build()
                .toJsonString();
        // 默认会话设置
        String defaultSessionSetting = TerminalPreferenceModel.SessionSettingModel.builder()
                .terminalEmulationType(TerminalType.XTERM.getType())
                .scrollBackLine(1000)
                .build()
                .toJsonString();
        // 默认配置
        return TerminalPreferenceModel.builder()
                .newConnectionType("group")
                .theme(new JSONObject())
                .displaySetting(JSONObject.parseObject(defaultDisplaySetting))
                .actionBarSetting(new JSONObject())
                .interactSetting(JSONObject.parseObject(defaultInteractSetting))
                .pluginsSetting(JSONObject.parseObject(defaultPluginsSetting))
                .sessionSetting(JSONObject.parseObject(defaultSessionSetting))
                .build();
    }

}
