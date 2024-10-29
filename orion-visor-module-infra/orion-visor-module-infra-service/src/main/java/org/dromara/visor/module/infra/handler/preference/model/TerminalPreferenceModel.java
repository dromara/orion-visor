/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.module.infra.handler.preference.model;

import cn.orionsec.kit.lang.able.IJsonObject;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;

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
public class TerminalPreferenceModel implements GenericsDataModel {

    /**
     * 新建连接类型
     */
    private String newConnectionType;

    /**
     * 终端主题
     */
    private JSONObject theme;

    /**
     * 显示设置
     */
    private JSONObject displaySetting;

    /**
     * 操作栏设置
     */
    private JSONObject actionBarSetting;

    /**
     * 右键菜单设置
     */
    private List<String> rightMenuSetting;

    /**
     * 交互设置
     */
    private JSONObject interactSetting;

    /**
     * 插件设置
     */
    private JSONObject pluginsSetting;

    /**
     * 会话设置
     */
    private JSONObject sessionSetting;

    /**
     * 快捷键设置
     */
    private JSONObject shortcutSetting;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplaySettingModel implements IJsonObject {

        /**
         * 字体样式
         */
        private String fontFamily;

        /**
         * 字体大小
         */
        private Integer fontSize;

        /**
         * 行高
         */
        private Double lineHeight;

        /**
         * 字间距
         */
        private Integer letterSpacing;

        /**
         * 文本字重
         */
        private String fontWeight;

        /**
         * 加粗字重
         */
        private String fontWeightBold;

        /**
         * 光标样式
         */
        private String cursorStyle;

        /**
         * 光标闪烁
         */
        private Boolean cursorBlink;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InteractSettingModel implements IJsonObject {

        /**
         * 快速滚动
         */
        private Boolean fastScrollModifier;

        /**
         * 点击移动光标
         */
        private Boolean altClickMovesCursor;

        /**
         * 右键选中词条
         */
        private Boolean rightClickSelectsWord;

        /**
         * 选中自动复制
         */
        private Boolean selectionChangeCopy;

        /**
         * 复制去除空格
         */
        private Boolean copyAutoTrim;

        /**
         * 粘贴去除空格
         */
        private Boolean pasteAutoTrim;

        /**
         * 右键粘贴
         */
        private Boolean rightClickPaste;

        /**
         * 启用右键菜单
         */
        private Boolean enableRightClickMenu;

        /**
         * 启用响铃
         */
        private Boolean enableBell;

        /**
         * 单词分隔符
         */
        private String wordSeparator;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PluginsSettingModel implements IJsonObject {

        /**
         * 超链接插件
         */
        private Boolean enableWeblinkPlugin;

        /**
         * WebGL 渲染插件
         */
        private Boolean enableWebglPlugin;

        /**
         * unicode11 插件
         */
        private Boolean enableUnicodePlugin;

        /**
         * 图片渲染插件
         */
        private Boolean enableImagePlugin;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionSettingModel implements IJsonObject {

        /**
         * 伪终端类型
         */
        private String terminalEmulationType;

        /**
         * 保存在缓冲区的行数
         */
        private Integer scrollBackLine;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortcutSettingModel implements IJsonObject {

        /**
         * 是否启用
         */
        private Boolean enabled;

        /**
         * 快捷键定义
         */
        private List<ShortcutKeysModel> keys;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionBarSettingModel implements IJsonObject {

        /**
         * 命令输入框
         */
        private Boolean commandInput;

        /**
         * 连接状态
         */
        private Boolean connectStatus;

        /**
         * 去顶部
         */
        private Boolean toTop;

        /**
         * 去底部
         */
        private Boolean toBottom;

        /**
         * 全选
         */
        private Boolean selectAll;

        /**
         * 搜索
         */
        private Boolean search;

        /**
         * 复制
         */
        private Boolean copy;

        /**
         * 粘贴
         */
        private Boolean paste;

        /**
         * 中断
         */
        private Boolean interrupt;

        /**
         * 回车
         */
        private Boolean enter;

        /**
         * 增大字号
         */
        private Boolean fontSizePlus;

        /**
         * 减小字号
         */
        private Boolean fontSizeSubtract;

        /**
         * 命令编辑器
         */
        private Boolean commandEditor;

        /**
         * 打开 SFTP
         */
        private Boolean openSftp;

        /**
         * 上传文件
         */
        private Boolean uploadFile;

        /**
         * 清空
         */
        private Boolean clear;

        /**
         * 断开连接
         */
        private Boolean disconnect;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortcutKeysModel implements IJsonObject {

        /**
         * 快捷键操作
         */
        private String item;

        /**
         * ctrl 按键
         */
        private Boolean ctrlKey;

        /**
         * shift 按键
         */
        private Boolean shiftKey;

        /**
         * alt 按键
         */
        private Boolean altKey;

        /**
         * 实际按键
         */
        private String code;

        /**
         * 是否启用
         */
        private Boolean enabled;

    }

}
