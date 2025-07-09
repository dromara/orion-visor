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
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.common.config.GuacdConfig;
import org.dromara.visor.module.terminal.handler.guacd.GuacdTunnel;
import org.dromara.visor.module.terminal.handler.guacd.IGuacdTunnel;
import org.dromara.visor.module.terminal.handler.guacd.constant.GuacdConst;
import org.dromara.visor.module.terminal.handler.guacd.constant.GuacdProtocol;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelExtra;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionVncConfig;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;

/**
 * vnc 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/30 17:44
 */
@Slf4j
public class VncSession extends AbstractGuacdSession<TerminalSessionVncConfig> implements IVncSession {

    private final GuacdConfig guacdConfig;

    public VncSession(TerminalChannelProps props,
                      IGuacdTerminalSender sender,
                      TerminalSessionVncConfig config,
                      GuacdConfig guacdConfig) {
        super(props, sender, config);
        this.guacdConfig = guacdConfig;
    }

    @Override
    protected IGuacdTunnel createTunnel() {
        return new GuacdTunnel(GuacdProtocol.VNC, sessionId, guacdConfig.getHost(), guacdConfig.getPort());
    }

    @Override
    protected void setTunnelParams() {
        super.setTunnelParams();
        // 设置额外参数
        TerminalChannelExtra extra = props.getExtra();
        // 时区
        tunnel.timezone(config.getTimezone());
        // 显示设置
        tunnel.setParameter(GuacdConst.COLOR_DEPTH, extra.getColorDepth());
        tunnel.setParameter(GuacdConst.FORCE_LOSSLESS, extra.getForceLossless());
        tunnel.setParameter(GuacdConst.COMPRESS_LEVEL, extra.getCompressLevel());
        tunnel.setParameter(GuacdConst.QUALITY_LEVEL, extra.getQualityLevel());
        // 交换红蓝
        tunnel.setParameter(GuacdConst.SWAP_RED_BLUE, config.getSwapRedBlue());
        // 光标设置
        tunnel.setParameter(GuacdConst.CURSOR, extra.getCursor());
        // 编码设置
        tunnel.setParameter(GuacdConst.CLIPBOARD_ENCODING, config.getClipboardEncoding());
    }

    @Override
    protected boolean isLowBandwidthMode() {
        return Booleans.isTrue(config.getLowBandwidthMode());
    }

}
