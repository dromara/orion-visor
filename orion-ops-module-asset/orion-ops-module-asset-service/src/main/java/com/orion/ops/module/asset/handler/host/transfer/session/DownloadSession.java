package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpFile;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.handler.host.transfer.enums.TransferReceiverType;
import com.orion.ops.module.asset.handler.host.transfer.utils.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.InputStream;

/**
 * 下载会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:25
 */
@Slf4j
public class DownloadSession extends TransferHostSession implements IDownloadSession {

    private InputStream inputStream;

    public DownloadSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        super(connectInfo, sessionStore, channel);
    }

    @Override
    public void startDownload(String path) {
        try {
            // 检查连接
            this.init();
            // 检查文件是否存在
            SftpFile file = executor.getFile(path);
            Valid.notNull(file, ErrorMessage.FILE_ABSENT);
            // 打开输入流
            this.inputStream = executor.openInputStream(path);
        } catch (Exception e) {
            log.error("DownloadSession.uploadStart error", e);
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_ERROR, e);
            return;
        }
        // 异步读取文件内容  FIXME bug
        AssetThreadPools.TERMINAL_SCHEDULER.execute(() -> {
            try {
                byte[] buffer = new byte[Const.BUFFER_KB_32];
                int len;
                // 响应文件内容
                while ((len = this.inputStream.read(buffer)) != -1) {
                    this.channel.sendMessage(new BinaryMessage(buffer, 0, len, true));
                }
                // 响应结果
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_FINISH, null);
            } catch (Exception e) {
                log.error("DownloadSession.transfer error", e);
                // 响应结果
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_ERROR, e);
            } finally {
                this.closeStream();
            }
        });
    }

    @Override
    public void abortDownload() {
        log.info("DownloadSession.abortDownload");
        // 关闭流
        Streams.close(inputStream);
        // 响应结果
        TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_FINISH, null);
    }

    @Override
    public void close() {
        super.close();
        this.closeStream();
    }

    /**
     * 关闭流
     */
    private void closeStream() {
        // 关闭流
        Streams.close(inputStream);
        this.inputStream = null;
    }

}
