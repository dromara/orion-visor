package com.orion.ops.module.asset.handler.host.transfer.handler;

import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.handler.host.transfer.enums.TransferOperatorType;
import com.orion.ops.module.asset.handler.host.transfer.enums.TransferReceiverType;
import com.orion.ops.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.ops.module.asset.handler.host.transfer.session.*;
import com.orion.ops.module.asset.handler.host.transfer.utils.TransferUtils;
import com.orion.ops.module.asset.service.HostTerminalService;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 传输处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 20:57
 */
@Slf4j
public class TransferHandler implements ITransferHandler {

    private static final HostTerminalService hostTerminalService = SpringHolder.getBean(HostTerminalService.class);

    private final Long userId;

    private final WebSocketSession channel;

    /**
     * 当前会话
     */
    private ITransferHostSession currentSession;

    /**
     * 会话列表
     */
    private final ConcurrentHashMap<String, ITransferHostSession> sessions;

    public TransferHandler(WebSocketSession channel) {
        this.channel = channel;
        this.userId = (Long) channel.getAttributes().get(ExtraFieldConst.USER_ID);
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public void handleMessage(TransferOperatorRequest payload) {
        // 解析消息类型
        TransferOperatorType type = TransferOperatorType.of(payload.getType());
        // 获取会话
        if (!this.getAndInitSession(payload, type)) {
            return;
        }
        // 处理消息
        switch (type) {
            case UPLOAD_START:
                // 开始上传
                ((IUploadSession) currentSession).startUpload(payload.getPath());
                break;
            case UPLOAD_FINISH:
                // 上传完成
                ((IUploadSession) currentSession).uploadFinish();
                break;
            case UPLOAD_ERROR:
                // 上传失败
                ((IUploadSession) currentSession).uploadError();
                break;
            case DOWNLOAD_START:
                // 开始下载
                ((IDownloadSession) currentSession).startDownload(payload.getPath());
                break;
            case DOWNLOAD_ABORT:
                // 中断下载
                ((IDownloadSession) currentSession).abortDownload();
                break;
            default:
                break;
        }
    }

    @Override
    public void putContent(byte[] content) {
        ((IUploadSession) currentSession).putContent(content);
    }

    /**
     * 获取并且初始化会话
     *
     * @param payload payload
     * @param type    type
     * @return success
     */
    private boolean getAndInitSession(TransferOperatorRequest payload, TransferOperatorType type) {
        Long hostId = payload.getHostId();
        String sessionKey = hostId + "_" + type.getOperator();
        try {
            // 获取会话
            ITransferHostSession session = sessions.get(sessionKey);
            if (session == null) {
                // 获取主机信息
                HostTerminalConnectDTO connectInfo = hostTerminalService.getTerminalConnectInfo(hostId, this.userId, HostConnectTypeEnum.SFTP);
                SessionStore sessionStore = hostTerminalService.openSessionStore(connectInfo);
                // 打开会话并初始化
                if (TransferOperatorType.UPLOAD.equals(type.getOperator())) {
                    // 上传操作
                    session = new UploadSession(connectInfo, sessionStore, this.channel);
                } else if (TransferOperatorType.DOWNLOAD.equals(type.getOperator())) {
                    // 下载操作
                    session = new DownloadSession(connectInfo, sessionStore, this.channel);
                } else {
                    throw Exceptions.invalidArgument(ErrorMessage.UNKNOWN_TYPE);
                }
                session.init();
                sessions.put(sessionKey, session);
            }
            this.currentSession = session;
            return true;
        } catch (Exception e) {
            log.error("TransferHandler.getAndInitSession error", e);
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiverType.NEXT_TRANSFER, e);
            return false;
        }
    }

    @Override
    public void close() {
        sessions.values().forEach(Streams::close);
    }

}
