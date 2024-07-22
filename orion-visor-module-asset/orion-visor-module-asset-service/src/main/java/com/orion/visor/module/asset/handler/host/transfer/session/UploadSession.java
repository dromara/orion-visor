package com.orion.visor.module.asset.handler.host.transfer.session;

import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.visor.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferReceiver;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.visor.module.asset.handler.host.transfer.utils.TransferUtils;
import com.orion.visor.module.asset.utils.SftpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 上传会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:04
 */
@Slf4j
public class UploadSession extends TransferSession {

    protected OutputStream outputStream;

    public UploadSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        super(connectInfo, sessionStore, channel);
    }

    @Override
    public void onStart(TransferOperatorRequest request) {
        super.onStart(request);
        try {
            log.info("UploadSession.startUpload start channelId: {}, path: {}", channelId, path);
            // 保存操作日志
            this.saveOperatorLog(HostTerminalOperatorType.SFTP_UPLOAD, path);
            // 检查连接
            this.init();
            // 检查文件是否存在
            SftpUtils.checkUploadFilePresent(SFTP_CONFIG, executor, path);
            // 打开输出流
            this.outputStream = executor.openOutputStream(path);
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.NEXT_PART, null);
            log.info("UploadSession.startUpload transfer channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("UploadSession.startUpload error channelId: {}, path: {}", channelId, path, e);
            this.closeStream();
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.ERROR, e);
        }
    }

    @Override
    public void handleBinary(byte[] bytes) {
        try {
            // 写入内容
            outputStream.write(bytes);
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.NEXT_PART, null);
        } catch (IOException e) {
            log.error("UploadSession.handleBinary error channelId: {}", channel.getId(), e);
            this.closeStream();
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.ERROR, e);
        }
    }

    @Override
    protected void closeStream() {
        if (this.outputStream != null) {
            // 关闭流
            Streams.close(outputStream);
            this.outputStream = null;
        }
    }

}
