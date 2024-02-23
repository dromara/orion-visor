package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.utils.Threads;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpFile;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
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
        String channelId = channel.getId();
        try {
            log.info("DownloadSession.startDownload open start channelId: {}, path: {}", channelId, path);
            // 保存操作日志
            this.saveOperatorLog(HostTerminalOperatorType.SFTP_DOWNLOAD, path);
            // 检查连接
            this.init();
            // 检查文件是否存在
            SftpFile file = executor.getFile(path);
            Valid.notNull(file, ErrorMessage.FILE_ABSENT);
            // 打开输入流
            this.inputStream = executor.openInputStream(path);
            log.info("DownloadSession.startDownload open success channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("DownloadSession.startDownload open error channelId: {}, path: {}", channelId, path, e);
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_ERROR, e);
            return;
        }
        // 异步读取文件内容
        AssetThreadPools.SFTP_DOWNLOAD_SCHEDULER.execute(() -> {
            Exception ex = null;
            try {
                byte[] buffer = new byte[Const.BUFFER_KB_32];
                int len;
                // 响应文件内容
                while (this.inputStream != null && (len = this.inputStream.read(buffer)) != -1) {
                    this.channel.sendMessage(new BinaryMessage(buffer, 0, len, true));
                }
                log.info("DownloadSession.download finish channelId: {}, path: {}", channelId, path);
            } catch (Exception e) {
                log.error("DownloadSession.download error channelId: {}, path: {}", channelId, path, e);
                ex = e;
            }
            // 关闭等待 jsch 内部处理
            Threads.sleep(100);
            this.closeStream();
            Threads.sleep(100);
            // 响应结果
            if (ex == null) {
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_FINISH, null);
            } else {
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_ERROR, ex);
            }
        });
    }

    @Override
    public void abortDownload() {
        log.info("DownloadSession.abortDownload channelId: {}", channel.getId());
        // 关闭流
        this.closeStream();
    }

    @Override
    protected void closeStream() {
        // 关闭 inputStream 会被阻塞 ??..?? 只能关闭 executor
        Streams.close(this.executor);
        this.executor = null;
        this.inputStream = null;
    }

}
