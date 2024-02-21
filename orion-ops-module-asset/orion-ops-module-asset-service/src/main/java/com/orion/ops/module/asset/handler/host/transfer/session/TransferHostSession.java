package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.net.host.sftp.SftpFile;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 主机传输会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:12
 */
public class TransferHostSession implements ITransferHostSession {

    private final HostTerminalConnectDTO connectInfo;

    private final SessionStore sessionStore;

    private SftpExecutor executor;

    private OutputStream currentOutputStream;

    public TransferHostSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore) {
        this.connectInfo = connectInfo;
        this.sessionStore = sessionStore;
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
    public void startUpload(String path) throws IOException {
        // 检查连接
        this.init();
        SftpFile file = executor.getFile(path);
        if (file != null) {
            // 文件存在则重命名
            executor.move(path, file.getName() + "_bk_" + System.currentTimeMillis());
        }
        // 打开输出流
        this.currentOutputStream = executor.openOutputStream(path);
    }

    @Override
    public void putContent(byte[] bytes) throws IOException {
        currentOutputStream.write(bytes);
    }

    @Override
    public void putFinish() {
        Streams.close(currentOutputStream);
        this.currentOutputStream = null;
    }

    @Override
    public void close() {
        Streams.close(executor);
        Streams.close(sessionStore);
        Streams.close(currentOutputStream);
    }

}
