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
package org.dromara.visor.module.terminal.handler.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端连接额外信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/31 15:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalChannelExtra {

    /**
     * 连接类型
     */
    private String connectType;

    // -------------------- ssh --------------------

    /**
     * 伪终端类型
     */
    private String terminalType;

    // -------------------- sftp --------------------

    /**
     * 文件预览大小
     */
    private Integer filePreviewSize;

    // -------------------- rdp --------------------

    /**
     * 启用音频输入
     */
    private Boolean enableAudioInput;

    /**
     * 启用音频输出
     */
    private Boolean enableAudioOutput;

    /**
     * 颜色深度
     */
    private Integer colorDepth;

    /**
     * 无损压缩
     */
    private Boolean forceLossless;

    /**
     * 启用壁纸
     */
    private Boolean enableWallpaper;

    /**
     * 启用主题
     */
    private Boolean enableTheming;

    /**
     * 启动平滑字体
     */
    private Boolean enableFontSmoothing;

    /**
     * 启用窗口拖动
     */
    private Boolean enableFullWindowDrag;

    /**
     * 启用桌面合成
     */
    private Boolean enableDesktopComposition;

    /**
     * 启用菜单动画
     */
    private Boolean enableMenuAnimations;

    /**
     * 禁用位图缓存
     */
    private Boolean disableBitmapCaching;

    /**
     * 禁用离屏缓存
     */
    private Boolean disableOffscreenCaching;

    /**
     * 禁用字形缓存
     */
    private Boolean disableGlyphCaching;

    /**
     * 禁用图形加速
     */
    private Boolean disableGfx;

    /**
     * 驱动挂载模式
     */
    private String driveMountMode;

    // -------------------- vnc --------------------

    /**
     * 交换红蓝
     */
    private Boolean swapRedBlue;

    /**
     * 光标
     */
    private String cursor;

    /**
     * 压缩等级
     */
    private Integer compressLevel;

    /**
     * 质量等级
     */
    private Integer qualityLevel;

}
