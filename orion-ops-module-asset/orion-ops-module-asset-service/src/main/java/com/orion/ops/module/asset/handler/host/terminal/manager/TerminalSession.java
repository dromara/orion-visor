package com.orion.ops.module.asset.handler.host.terminal.manager;

import com.orion.lang.able.SafeCloseable;
import com.orion.net.host.SessionStore;
import com.orion.net.host.ssh.shell.ShellExecutor;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
public class TerminalSession implements SafeCloseable {

    private final WebSocketSession session;

    private final HostTerminalConnectDTO connect;

    private final SessionStore sessionStore;

    private ShellExecutor executor;

    public TerminalSession(WebSocketSession session,
                           HostTerminalConnectDTO connect,
                           SessionStore sessionStore) {
        this.session = session;
        this.connect = connect;
        this.sessionStore = sessionStore;
    }

    /**
     * 连接
     *
     * @param cols cols
     * @param rows rows
     */
    public void connect(int cols, int rows) {
        this.executor = sessionStore.getShellExecutor();
        executor.size(cols, rows);
    }

    @Override
    public void close() {
    }

}
