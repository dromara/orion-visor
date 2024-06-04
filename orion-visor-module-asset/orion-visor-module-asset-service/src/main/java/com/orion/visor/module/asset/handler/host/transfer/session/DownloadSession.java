package com.orion.visor.module.asset.handler.host.transfer.session;

import com.orion.lang.define.wrapper.Ref;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpFile;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.module.asset.define.AssetThreadPools;
import com.orion.visor.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferReceiverType;
import com.orion.visor.module.asset.handler.host.transfer.utils.TransferUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 下载会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:25
 */
@Slf4j
public class DownloadSession extends TransferHostSession implements IDownloadSession {

    @Getter
    private String path;

    private InputStream inputStream;

    public DownloadSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        super(connectInfo, sessionStore, channel);
    }

    @Override
    public void downloadInit(String path, String token) {
        this.path = path;
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
            if (file.getSize() == 0L) {
                // 文件为空
                log.info("DownloadSession.startDownload file empty channelId: {}, path: {}", channelId, path);
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_FINISH, null);
                return;
            }
            // 打开输入流
            this.inputStream = executor.openInputStream(path);
            // 响应开始下载
            TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_START, null, e -> {
                e.setChannelId(channelId);
                e.setTransferToken(token);
            });
            log.info("DownloadSession.startDownload open success channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("DownloadSession.startDownload open error channelId: {}, path: {}", channelId, path, e);
            // 响应下载失败
            TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_ERROR, e);
        }
    }

    @Override
    public void abortDownload() {
        log.info("DownloadSession.abortDownload channelId: {}", channel.getId());
        // 关闭流
        this.closeStream();
    }

    @Override
    public void writeTo(OutputStream outputStream) {
        String channelId = channel.getId();
        Ref<Exception> ex = new Ref<>();
        try {
            byte[] buffer = new byte[Const.BUFFER_KB_32];
            int len;
            int i = 0;
            int size = 0;
            // 响应文件内容
            while (this.inputStream != null && (len = this.inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                size += len;
                // 不要每次都 flush 和 send > 1mb
                if (i == 32) {
                    i = 0;
                }
                // 首次触发
                if (i == 0) {
                    this.flushAndSendProgress(outputStream, size);
                }
                i++;
            }
            // 最后一次也要 flush
            if (i != 0) {
                this.flushAndSendProgress(outputStream, size);
            }
            log.info("DownloadSession.download finish channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("DownloadSession.download error channelId: {}, path: {}", channelId, path, e);
            ex.set(e);
        }
        // 异步关闭
        AssetThreadPools.TERMINAL_OPERATOR.execute(() -> {
            // 关闭等待 jsch 内部处理
            Threads.sleep(100);
            this.closeStream();
            Threads.sleep(100);
            // 响应结果
            Exception e = ex.getValue();
            if (e == null) {
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_FINISH, null);
            } else {
                TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_ERROR, e);
            }
        });
    }

    /**
     * 刷流 & 发送进度
     *
     * @param outputStream outputStream
     * @param size         size
     * @throws IOException IOException
     */
    private void flushAndSendProgress(OutputStream outputStream, int size) throws IOException {
        // flush
        outputStream.flush();
        // send
        TransferUtils.sendMessage(this.channel, TransferReceiverType.DOWNLOAD_PROGRESS, null, e -> e.setCurrentSize(size));
    }

    @Override
    protected void closeStream() {
        // 关闭 inputStream 可能会被阻塞 ???...??? 只能关闭 executor
        this.path = null;
        Streams.close(this.executor);
        this.executor = null;
        this.inputStream = null;
    }

}
