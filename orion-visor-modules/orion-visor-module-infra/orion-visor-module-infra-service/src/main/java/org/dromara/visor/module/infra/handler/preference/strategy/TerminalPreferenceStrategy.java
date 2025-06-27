/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.handler.preference.strategy;

import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.net.host.ssh.TerminalType;
import com.alibaba.fastjson.JSONObject;
import org.dromara.visor.common.handler.data.strategy.AbstractGenericsDataStrategy;
import org.dromara.visor.module.infra.handler.preference.model.TerminalPreferenceModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 终端偏好处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/8 14:46
 */
@Component
public class TerminalPreferenceStrategy extends AbstractGenericsDataStrategy<TerminalPreferenceModel> {

    public TerminalPreferenceStrategy() {
        super(TerminalPreferenceModel.class);
    }

    @Override
    public TerminalPreferenceModel getDefault() {
        return TerminalPreferenceModel.builder()
                // 连接类型
                .newConnectionType("group")
                // ssh 主题
                .theme(new JSONObject())
                // ssh 显示设置
                .sshDisplaySetting(JSONObject.parseObject(this.getDefaultSshDisplaySetting()))
                // rdp 图形化设置
                .rdpGraphSetting(JSONObject.parseObject(this.getDefaultRdpGraphSetting()))
                // ssh 操作栏设置
                .sshActionBarSetting(JSONObject.parseObject(this.getDefaultSshActionBarSetting()))
                // rdp 操作栏设置
                .rdpActionBarSetting(JSONObject.parseObject(this.getDefaultRdpActionBarSetting()))
                // ssh 右键菜单设置
                .rightMenuSetting(this.getDefaultRightMenuSetting())
                // 交互设置
                .interactSetting(JSONObject.parseObject(this.getDefaultInteractSetting()))
                // 插件设置
                .pluginsSetting(JSONObject.parseObject(this.getDefaultPluginsSetting()))
                // 会话设置
                .sessionSetting(JSONObject.parseObject(this.getDefaultSessionSetting()))
                // 快捷键设置
                .shortcutSetting(JSONObject.parseObject(this.getDefaultShortcutSetting()))
                .build();
    }

    /**
     * 获取 ssh 显示默认设置
     *
     * @return setting
     */
    private String getDefaultSshDisplaySetting() {
        return TerminalPreferenceModel.SshDisplaySettingModel
                .builder()
                .fontFamily("_")
                .fontSize(14)
                .lineHeight(1.20)
                .letterSpacing(0)
                .fontWeight("normal")
                .fontWeightBold("bold")
                .cursorStyle("bar")
                .cursorBlink(true)
                .build()
                .toJsonString();
    }

    /**
     * 获取 rdp 图形化默认设置
     *
     * @return setting
     */
    private String getDefaultRdpGraphSetting() {
        return TerminalPreferenceModel.RdpGraphSettingModel.builder()
                .displaySize("fit")
                .displayWidth(0)
                .displayHeight(0)
                .colorDepth(24)
                .enableAudioInput(false)
                .enableAudioOutput(true)
                .forceLossless(true)
                .enableWallpaper(true)
                .enableTheming(true)
                .enableFontSmoothing(true)
                .enableFullWindowDrag(true)
                .enableDesktopComposition(true)
                .enableMenuAnimations(false)
                .disableBitmapCaching(false)
                .disableOffscreenCaching(false)
                .disableGlyphCaching(false)
                .build()
                .toJsonString();
    }

    /**
     * 获取 ssh 操作栏默认设置
     *
     * @return setting
     */
    private String getDefaultSshActionBarSetting() {
        // 操作栏设置
        return TerminalPreferenceModel.SshActionBarSettingModel.builder()
                .connectStatus(true)
                .toTop(false)
                .toBottom(false)
                .selectAll(false)
                .search(true)
                .copy(true)
                .paste(true)
                .interrupt(false)
                .enter(false)
                .fontSizePlus(false)
                .fontSizeSubtract(false)
                .openSftp(true)
                .uploadFile(true)
                .clear(true)
                .disconnect(false)
                .build()
                .toJsonString();
    }

    /**
     * 获取 rdp 操作栏默认设置
     *
     * @return setting
     */
    private String getDefaultRdpActionBarSetting() {
        return TerminalPreferenceModel.RdpActionBarSettingModel.builder()
                .position("top")
                .display(true)
                .combinationKey(true)
                .clipboard(true)
                .upload(true)
                .saveRdp(true)
                .disconnect(true)
                .close(true)
                .build()
                .toJsonString();
    }

    /**
     * 获取 ssh 右键菜单默认设置
     *
     * @return setting
     */
    private List<String> getDefaultRightMenuSetting() {
        return Lists.of("selectAll", "copy", "paste", "search", "clear");
    }

    /**
     * 获取默认交互设置
     *
     * @return setting
     */
    private String getDefaultInteractSetting() {
        return TerminalPreferenceModel.InteractSettingModel.builder()
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
    }

    /**
     * 获取默认插件设置
     *
     * @return setting
     */
    private String getDefaultPluginsSetting() {
        return TerminalPreferenceModel.PluginsSettingModel.builder()
                .enableWeblinkPlugin(true)
                .enableWebglPlugin(true)
                .enableUnicodePlugin(true)
                .enableImagePlugin(false)
                .build()
                .toJsonString();
    }

    /**
     * 获取默认会话设置
     *
     * @return setting
     */
    private String getDefaultSessionSetting() {
        return TerminalPreferenceModel.SessionSettingModel.builder()
                .terminalEmulationType(TerminalType.XTERM.getType())
                .scrollBackLine(1000)
                .build()
                .toJsonString();
    }

    /**
     * 获取默认快捷键设置
     *
     * @return setting
     */
    private String getDefaultShortcutSetting() {
        return TerminalPreferenceModel.ShortcutSettingModel.builder()
                .enabled(true)
                .keys(Lists.of(
                        // 全局快捷键
                        new TerminalPreferenceModel.ShortcutKeysModel("closeTab", true, true, true, "KeyW", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("changeToPrevTab", true, true, true, "BracketLeft", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("changeToNextTab", true, true, true, "BracketRight", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("openNewConnectTab", true, true, true, "KeyN", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("openCommandSnippet", true, true, true, "KeyC", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("openPathBookmark", true, true, true, "KeyP", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("openTransferList", true, true, true, "KeyT", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("openCommandBar", true, true, true, "KeyI", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("screenshot", true, true, true, "KeyS", true),
                        // 会话快捷键
                        new TerminalPreferenceModel.ShortcutKeysModel("openNewConnectModal", true, false, true, "KeyN", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("copySession", true, false, true, "KeyO", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("closeSession", true, false, true, "KeyW", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("changeToPrevSession", true, false, true, "BracketLeft", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("changeToNextSession", true, false, true, "BracketRight", true),
                        // 终端快捷键
                        new TerminalPreferenceModel.ShortcutKeysModel("copy", true, true, false, "KeyC", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("paste", true, true, false, "Insert", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("toTop", true, true, false, "ArrowUp", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("toBottom", true, true, false, "ArrowDown", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("selectAll", true, true, false, "KeyA", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("search", true, true, false, "KeyF", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("uploadFile", true, true, false, "KeyU", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("fontSizePlus", true, false, true, "Equal", true),
                        new TerminalPreferenceModel.ShortcutKeysModel("fontSizeSubtract", true, false, true, "Minus", true)
                ))
                .build()
                .toJsonString();
    }

}
