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

import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.terminal.enums.TerminalConnectStatusEnum;
import org.dromara.visor.module.terminal.handler.terminal.constant.SessionCloseCode;
import org.dromara.visor.module.terminal.handler.terminal.constant.TerminalMessage;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.ITerminalSessionConfig;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.dromara.visor.module.terminal.service.TerminalConnectLogService;

/**
 * 终端会话基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/4 16:51
 */
@Slf4j
public abstract class AbstractTerminalSession<S extends ITerminalSender, C extends ITerminalSessionConfig> implements ITerminalSession {

    protected final String sessionId;

    @Getter
    protected final TerminalChannelProps props;

    @Getter
    protected final S sender;

    protected final C config;

    @Getter
    protected volatile boolean closed;

    @Getter
    protected volatile boolean connected;

    protected Integer closeCode;

    protected String closeMessage;

    public AbstractTerminalSession(TerminalChannelProps props,
                                   S sender,
                                   C config) {
        this.sessionId = props.getId();
        this.props = props;
        this.sender = sender;
        this.config = config;
    }

    /**
     * 释放资源
     */
    protected abstract void releaseResource();

    @Override
    public void forceOffline() {
        log.info("terminal forceOffline {}", sessionId);
        // 设置为强制关闭
        this.setForcedOffline();
        // 关闭
        this.close();
    }

    @Override
    public void setForcedOffline() {
        this.closeCode = SessionCloseCode.FORCE_OFFLINE;
        this.closeMessage = TerminalMessage.FORCED_OFFLINE;
    }

    @Override
    public void setCloseStatus(Integer code, String message) {
        this.closeCode = code;
        this.closeMessage = message;
    }

    @Override
    public void close() {
        log.info("terminal close {}", sessionId);
        // 检查并且关闭
        if (closed) {
            return;
        }
        this.connected = false;
        this.closed = true;
        // 发送关闭信息
        try {
            this.sendCloseMessage();
        } catch (Exception e) {
            log.error("terminal close send error {}", sessionId, e);
        }
        // 释放资源
        try {
            this.releaseResource();
        } catch (Exception e) {
            log.error("terminal close release error {}", sessionId, e);
        }
        try {
            // 修改连接状态
            this.updateLogStatus();
        } catch (Exception e) {
            log.error("terminal close update error {}", sessionId, e);
        }
    }

    /**
     * 发送关闭消息
     */
    protected void sendCloseMessage() {
        log.info("TerminalSession close {}, forClose: {}, code: {}, message: {}", sessionId, this.closed, this.closeCode, closeMessage);
        // 发送关闭信息
        sender.sendClosed(Objects1.def(closeCode, SessionCloseCode.NORMAL), Objects1.def(closeMessage, TerminalMessage.CONNECTION_CLOSED));
    }

    /**
     * 修改连接日志状态
     */
    protected void updateLogStatus() {
        SpringHolder.getBean(TerminalConnectLogService.class)
                .updateStatusById(config.getLogId(), this.isForcedOffline() ? TerminalConnectStatusEnum.FORCE_OFFLINE : TerminalConnectStatusEnum.COMPLETE, null);
    }

    @Override
    public boolean isForcedOffline() {
        return SessionCloseCode.FORCE_OFFLINE.equals(closeCode);
    }

    @Override
    public Long getLogId() {
        return config.getLogId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ITerminalSessionConfig> T getConfig() {
        return (T) config;
    }

}
