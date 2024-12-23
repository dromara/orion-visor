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
package org.dromara.visor.module.asset.handler.host.terminal.session;

import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.enums.BooleanBit;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.asset.enums.TerminalConnectStatusEnum;
import org.dromara.visor.module.asset.handler.host.terminal.constant.TerminalMessage;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.TerminalCloseResponse;
import org.dromara.visor.module.asset.service.TerminalConnectLogService;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端会话基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/4 16:51
 */
@Slf4j
public abstract class TerminalSession implements ITerminalSession {

    @Getter
    protected final String sessionId;

    protected final WebSocketSession channel;

    @Getter
    protected final TerminalConfig config;

    @Getter
    protected volatile boolean closed;

    protected volatile boolean forceOffline;

    public TerminalSession(String sessionId, WebSocketSession channel, TerminalConfig config) {
        this.sessionId = sessionId;
        this.channel = channel;
        this.config = config;
    }

    /**
     * 释放资源
     */
    protected abstract void releaseResource();

    /**
     * 发送关闭消息
     */
    protected void sendCloseMessage() {
        log.info("TerminalSession close {}, forClose: {}, forceOffline: {}", sessionId, this.closed, this.forceOffline);
        // 发送关闭信息
        TerminalCloseResponse resp = TerminalCloseResponse.builder()
                .type(OutputTypeEnum.CLOSE.getType())
                .sessionId(this.sessionId)
                .forceClose(BooleanBit.of(this.forceOffline).getValue())
                .msg(this.forceOffline ? TerminalMessage.FORCED_OFFLINE : TerminalMessage.CONNECTION_CLOSED)
                .build();
        WebSockets.sendText(channel, OutputTypeEnum.CLOSE.format(resp));
    }

    @Override
    public void close() {
        log.info("terminal close {}", sessionId);
        // 检查并且关闭
        if (this.checkAndClose()) {
            // 修改状态
            SpringHolder.getBean(TerminalConnectLogService.class)
                    .updateStatusById(config.getLogId(), TerminalConnectStatusEnum.COMPLETE, null);
        }
    }

    @Override
    public void forceOffline() {
        log.info("terminal forceOffline {}", sessionId);
        this.forceOffline = true;
        // 关闭
        this.checkAndClose();
    }

    /**
     * 检查并且关闭会话
     *
     * @return close
     */
    private boolean checkAndClose() {
        if (closed) {
            return false;
        }
        this.closed = true;
        // 释放资源
        try {
            this.releaseResource();
        } catch (Exception e) {
            log.error("terminal release error {}", sessionId, e);
        }
        // 发送关闭信息
        try {
            this.sendCloseMessage();
        } catch (Exception e) {
            log.error("terminal send close error {}", sessionId, e);
        }
        return true;
    }

    @Override
    public String getChannelId() {
        return channel.getId();
    }

}
