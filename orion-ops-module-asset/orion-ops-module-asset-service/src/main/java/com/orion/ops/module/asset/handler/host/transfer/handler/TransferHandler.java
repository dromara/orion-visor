package com.orion.ops.module.asset.handler.host.transfer.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.handler.host.transfer.enums.TransferOperatorType;
import com.orion.ops.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.ops.module.asset.handler.host.transfer.model.TransferOperatorResponse;
import com.orion.ops.module.asset.handler.host.transfer.session.ITransferHostSession;
import com.orion.ops.module.asset.handler.host.transfer.session.TransferHostSession;
import com.orion.ops.module.asset.service.HostTerminalService;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
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
    private final ConcurrentHashMap<Long, ITransferHostSession> sessions;

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
        if (!this.getAndInitSession(payload)) {
            return;
        }
        // 处理消息
        switch (type) {
            case UPLOAD_START:
                // 准备上传
                this.uploadStart(payload);
                break;
            case UPLOAD_FINISH:
                // 上传完成
                this.uploadFinish();
                break;
            default:
                break;
        }
    }

    @Override
    public void putContent(byte[] content) {
        Exception ex = null;
        try {
            // 写入内容
            currentSession.putContent(content);
        } catch (IOException e) {
            ex = e;
            log.error("TransferHandler.putContent error", e);
            // 写入完成
            currentSession.putFinish();
        }
        // 响应结果
        TransferOperatorResponse resp = TransferOperatorResponse.builder()
                .type(TransferOperatorType.PROCESSED.getType())
                .success(ex == null)
                .msg(this.getErrorMessage(ex))
                .build();
        WebSockets.sendText(this.channel, JSON.toJSONString(resp));
    }

    /**
     * 准备上传
     *
     * @param payload payload
     */
    private void uploadStart(TransferOperatorRequest payload) {
        Exception ex = null;
        try {
            currentSession.startUpload(payload.getPath());
        } catch (Exception e) {
            ex = e;
            log.error("TransferHandler.uploadStart error", e);
        }
        // 响应结果
        TransferOperatorResponse resp = TransferOperatorResponse.builder()
                .type(TransferOperatorType.PROCESSED.getType())
                .success(ex == null)
                .msg(this.getErrorMessage(ex))
                .build();
        WebSockets.sendText(this.channel, JSON.toJSONString(resp));
    }

    /**
     * 上传完成
     */
    private void uploadFinish() {
        currentSession.putFinish();
        // 响应结果
        TransferOperatorResponse resp = TransferOperatorResponse.builder()
                .type(TransferOperatorType.PROCESSED.getType())
                .success(true)
                .build();
        WebSockets.sendText(this.channel, JSON.toJSONString(resp));
    }

    /**
     * 获取并且初始化会话
     *
     * @param payload payload
     * @return success
     */
    private boolean getAndInitSession(TransferOperatorRequest payload) {
        Long hostId = payload.getHostId();
        try {
            // 获取会话
            ITransferHostSession session = sessions.get(hostId);
            if (session == null) {
                // 获取主机信息
                HostTerminalConnectDTO connectInfo = hostTerminalService.getTerminalConnectInfo(hostId, this.userId, HostConnectTypeEnum.SFTP);
                SessionStore sessionStore = hostTerminalService.openSessionStore(connectInfo);
                // 打开会话并初始化
                session = new TransferHostSession(connectInfo, sessionStore);
                session.init();
                this.currentSession = session;
                sessions.put(hostId, session);
            }
            return true;
        } catch (Exception e) {
            log.error("TransferHandler.getAndInitSession error", e);
            // 响应结果
            TransferOperatorResponse resp = TransferOperatorResponse.builder()
                    .type(TransferOperatorType.PROCESSED.getType())
                    .success(false)
                    .msg(this.getErrorMessage(e))
                    .build();
            WebSockets.sendText(this.channel, JSON.toJSONString(resp));
            return false;
        }
    }

    /**
     * 获取错误信息
     *
     * @param ex ex
     * @return msg
     */
    private String getErrorMessage(Exception ex) {
        if (ex == null) {
            return null;
        }
        if (ex instanceof InvalidArgumentException) {
            return ex.getMessage();
        }
        return ErrorMessage.OPERATE_ERROR;
    }

    @Override
    public void close() {
        sessions.values().forEach(Streams::close);
    }

}
