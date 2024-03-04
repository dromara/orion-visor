package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

    protected volatile boolean close;

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

    @Override
    public void close() {
        log.info("terminal close {}", sessionId);
        // 检查并且关闭
        if (this.checkAndClose()) {
            // 修改状态
            SpringHolder.getBean(HostConnectLogService.class).updateStatusByToken(sessionId, HostConnectStatusEnum.COMPLETE);
        }
    }

    @Override
    public void forceOffline() {
        log.info("terminal forceOffline {}", sessionId);
        this.forceOffline = true;
        this.checkAndClose();
    }

    /**
     * 检查并且关闭会话
     *
     * @return close
     */
    private boolean checkAndClose() {
        if (close) {
            return false;
        }
        this.close = true;
        // 释放资源
        try {
            this.releaseResource();
        } catch (Exception e) {
            log.error("terminal release error {}", sessionId, e);
        }
        return true;
    }

    @Override
    public String getChannelId() {
        return channel.getId();
    }

}
