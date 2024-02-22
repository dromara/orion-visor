package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import org.springframework.web.socket.WebSocketSession;

/**
 * 主机传输会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:12
 */
public abstract class TransferHostSession implements ITransferHostSession {

    protected final HostTerminalConnectDTO connectInfo;

    protected final SessionStore sessionStore;

    protected final WebSocketSession channel;

    protected SftpExecutor executor;

    public TransferHostSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        this.connectInfo = connectInfo;
        this.sessionStore = sessionStore;
        this.channel = channel;
    }

    @Override
    public void init() {
        if (executor == null) {
            // 建立连接
            this.executor = sessionStore.getSftpExecutor(connectInfo.getFileNameCharset());
            executor.connect();
        } else {
            // 检查连接
            if (!this.executor.isConnected()) {
                executor.connect();
            }
        }
    }

    @Override
    public void close() {
        Streams.close(executor);
        Streams.close(sessionStore);
    }

}
