package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端 ssh 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
@Slf4j
public class SftpSession extends TerminalSession implements ISftpSession {

    private final TerminalConfig config;

    private final SessionStore sessionStore;

    private SftpExecutor executor;

    public SftpSession(String sessionId,
                       WebSocketSession channel,
                       SessionStore sessionStore,
                       TerminalConfig config) {
        super(sessionId, channel);
        this.sessionStore = sessionStore;
        this.config = config;
    }

    @Override
    public void connect() {
        // 打开 shell
        this.executor = sessionStore.getSftpExecutor(config.getFileNameCharset());
        executor.connect();
    }

    @Override
    public void keepAlive() {
        try {
            // 发送个信号 保证 socket 不自动关闭
            executor.sendSignal(Const.EMPTY);
        } catch (Exception e) {
            log.error("sftp keep-alive error {}", sessionId, e);
        }
    }

    @Override
    protected void releaseResource() {
        Streams.close(executor);
        Streams.close(sessionStore);
    }

}
