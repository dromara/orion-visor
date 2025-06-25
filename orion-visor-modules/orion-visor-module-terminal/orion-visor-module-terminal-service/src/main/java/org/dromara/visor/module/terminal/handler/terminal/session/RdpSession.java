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
package org.dromara.visor.module.terminal.handler.terminal.session;

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.time.Dates;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.AppConst;
import org.dromara.visor.common.utils.AesEncryptUtils;
import org.dromara.visor.module.common.config.GuacdConfig;
import org.dromara.visor.module.terminal.handler.guacd.GuacdTunnel;
import org.dromara.visor.module.terminal.handler.guacd.IGuacdTunnel;
import org.dromara.visor.module.terminal.handler.guacd.constant.GuacdConst;
import org.dromara.visor.module.terminal.handler.guacd.constant.GuacdProtocol;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelExtra;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionRdpConfig;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;

/**
 * rdp 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/30 17:44
 */
@Slf4j
public class RdpSession extends AbstractGuacdSession<TerminalSessionRdpConfig> implements IRdpSession {

    private final GuacdConfig guacdConfig;

    public RdpSession(TerminalChannelProps props,
                      IGuacdTerminalSender sender,
                      TerminalSessionRdpConfig config,
                      GuacdConfig guacdConfig) {
        super(props, sender, config);
        this.guacdConfig = guacdConfig;
    }

    @Override
    protected IGuacdTunnel createTunnel() {
        return new GuacdTunnel(GuacdProtocol.RDP, sessionId, guacdConfig.getHost(), guacdConfig.getPort());
    }

    @Override
    protected void setTunnelParams() {
        TerminalChannelExtra extra = props.getExtra();
        // 音频输入会导致无法连接先写死
        extra.setEnableAudioInput(false);
        // 设置低带宽模式
        if (Booleans.isTrue(config.getLowBandwidthMode())) {
            this.setLowBandwidthMode(extra);
        }
        // 主机信息
        tunnel.remote(config.getHostAddress(), config.getHostPort());
        // 身份信息
        tunnel.auth(config.getUsername(), AesEncryptUtils.decryptAsString(config.getPassword()));
        // 大小
        tunnel.size(config.getWidth(), config.getHeight(), config.getDpi());
        // 时区
        tunnel.timezone(config.getTimezone());
        // 忽略证书
        tunnel.setParameter(GuacdConst.IGNORE_CERT, true);
        // 域
        tunnel.setParameter(GuacdConst.DOMAIN, Strings.def(config.getDomain(), (String) null));
        // 系统布局
        tunnel.setParameter(GuacdConst.SERVER_LAYOUT, config.getKeyboardLayout());
        // 剪切板规范
        tunnel.setParameter(GuacdConst.NORMALIZE_CLIPBOARD, config.getClipboardNormalize());
        // 修改大小方法
        tunnel.setParameter(GuacdConst.RESIZE_METHOD, Booleans.isTrue(config.getVersionGt81()) ? GuacdConst.RESIZE_METHOD_DISPLAY_UPDATE : GuacdConst.RESIZE_METHOD_RECONNECT);
        // 显示设置
        tunnel.setParameter(GuacdConst.COLOR_DEPTH, extra.getColorDepth());
        tunnel.setParameter(GuacdConst.FORCE_LOSSLESS, extra.getForceLossless());
        // 偏好设置
        tunnel.setParameter(GuacdConst.ENABLE_WALLPAPER, extra.getEnableWallpaper());
        tunnel.setParameter(GuacdConst.ENABLE_THEMING, extra.getEnableTheming());
        tunnel.setParameter(GuacdConst.ENABLE_FONT_SMOOTHING, extra.getEnableFontSmoothing());
        tunnel.setParameter(GuacdConst.ENABLE_FULL_WINDOW_DRAG, extra.getEnableFullWindowDrag());
        tunnel.setParameter(GuacdConst.ENABLE_DESKTOP_COMPOSITION, extra.getEnableDesktopComposition());
        tunnel.setParameter(GuacdConst.ENABLE_MENU_ANIMATIONS, extra.getEnableMenuAnimations());
        tunnel.setParameter(GuacdConst.DISABLE_OFFSCREEN_CACHING, extra.getDisableOffscreenCaching());
        tunnel.setParameter(GuacdConst.DISABLE_GLYPH_CACHING, extra.getDisableGlyphCaching());
        tunnel.setParameter(GuacdConst.DISABLE_BITMAP_CACHING, extra.getDisableBitmapCaching());
        // 音频
        tunnel.setAudioMimeTypes(GuacdConst.AUDIO_MIMETYPES);
        tunnel.setParameter(GuacdConst.ENABLE_AUDIO_INPUT, extra.getEnableAudioInput());
        tunnel.setParameter(GuacdConst.DISABLE_AUDIO_OUTPUT, Booleans.isFalse(extra.getEnableAudioOutput()));
        // 驱动
        tunnel.setParameter(GuacdConst.CLIENT_NAME, AppConst.APP_NAME);
        tunnel.setParameter(GuacdConst.ENABLE_DRIVE, true);
        tunnel.setParameter(GuacdConst.CREATE_DRIVE_PATH, true);
        tunnel.setParameter(GuacdConst.DRIVE_NAME, GuacdConst.DRIVE_DRIVE_NAME);
        // 父文件夹必须存在 所以只能用 _ 分
        tunnel.setParameter(GuacdConst.DRIVE_PATH, Files1.getPath(guacdConfig.getDrivePath() + "/" + Dates.current(Dates.YMD2) + "_" + props.getUserId() + "_" + props.getHostId()));
        // 预连接
        String preConnectionId = config.getPreConnectionId();
        if (!Strings.isBlank(preConnectionId)) {
            tunnel.setParameter(GuacdConst.SECURITY, GuacdConst.SECURITY_VMCONNECT);
            tunnel.setParameter(GuacdConst.PRE_CONNECTION_ID, preConnectionId);
            tunnel.setParameter(GuacdConst.PRE_CONNECTION_BLOB, config.getPreConnectionBlob());
        }
        // RemoteApp
        String remoteApp = config.getRemoteApp();
        if (!Strings.isBlank(remoteApp)) {
            tunnel.setParameter(GuacdConst.REMOTE_APP, remoteApp);
            tunnel.setParameter(GuacdConst.REMOTE_APP_DIR, Strings.def(config.getRemoteAppDir(), (String) null));
            tunnel.setParameter(GuacdConst.REMOTE_APP_ARGS, Strings.def(config.getRemoteAppArgs(), (String) null));
        }
    }

    @Override
    public void resize(int width, int height) {
        config.setWidth(width);
        config.setHeight(height);
        tunnel.writeInstruction("size", String.valueOf(width), String.valueOf(height));
    }

    /**
     * 低带宽模式
     *
     * @param extra extra
     */
    private void setLowBandwidthMode(TerminalChannelExtra extra) {
        extra.setColorDepth(8);
        extra.setForceLossless(false);
        extra.setEnableWallpaper(false);
        extra.setEnableTheming(false);
        extra.setEnableFontSmoothing(false);
        extra.setEnableFullWindowDrag(false);
        extra.setEnableDesktopComposition(false);
        extra.setEnableMenuAnimations(false);
        extra.setDisableBitmapCaching(false);
        extra.setDisableGlyphCaching(false);
        extra.setDisableBitmapCaching(false);
        extra.setEnableAudioInput(false);
        extra.setEnableAudioOutput(false);
    }

}
