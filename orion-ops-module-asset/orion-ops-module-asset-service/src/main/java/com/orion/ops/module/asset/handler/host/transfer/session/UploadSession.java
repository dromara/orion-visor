package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.handler.host.transfer.enums.TransferReceiverType;
import com.orion.ops.module.asset.handler.host.transfer.utils.TransferUtils;
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
public class UploadSession extends TransferHostSession implements IUploadSession {

    private OutputStream outputStream;

    public UploadSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        super(connectInfo, sessionStore, channel);
    }

    @Override
    public void startUpload(String path) {
        String channelId = channel.getId();
        try {
            log.info("UploadSession.startUpload start channelId: {}, path: {}", channelId, path);
            // 保存操作日志
            this.saveOperatorLog(HostTerminalOperatorType.SFTP_UPLOAD, path);
            // 检查连接
            this.init();
            // 检查文件是否存在
            this.doCheckFilePresent(path);
            // 打开输出流
            this.outputStream = executor.openOutputStream(path);
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.UPLOAD_NEXT_BLOCK, null);
            log.info("UploadSession.startUpload transfer channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("UploadSession.startUpload error channelId: {}, path: {}", channelId, path, e);
            this.closeStream();
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.UPLOAD_ERROR, e);
        }
    }

    @Override
    public void putContent(byte[] bytes) {
        try {
            // 写入内容
            outputStream.write(bytes);
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.UPLOAD_NEXT_BLOCK, null);
        } catch (IOException e) {
            log.error("UploadSession.putContent error channelId: {}", channel.getId(), e);
            this.closeStream();
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.UPLOAD_ERROR, e);
        }
    }

    @Override
    public void uploadFinish() {
        log.info("UploadSession.uploadFinish channelId: {}", channel.getId());
        this.closeStream();
        // 响应结果
        TransferUtils.sendMessage(this.channel, TransferReceiverType.UPLOAD_FINISH, null);
    }

    @Override
    public void uploadError() {
        log.error("UploadSession.uploadError channelId: {}", channel.getId());
        this.closeStream();
        // 响应结果
        TransferUtils.sendMessage(this.channel, TransferReceiverType.UPLOAD_ERROR, new InvalidArgumentException((String) null));
    }

    @Override
    protected void closeStream() {
        // 关闭流
        Streams.close(outputStream);
        this.outputStream = null;
    }

}
