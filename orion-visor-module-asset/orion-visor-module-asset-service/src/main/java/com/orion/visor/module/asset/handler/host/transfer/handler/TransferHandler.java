package com.orion.visor.module.asset.handler.host.transfer.handler;

import com.orion.lang.id.UUIds;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.websocket.core.utils.WebSockets;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.handler.host.jsch.SessionStores;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferOperator;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferReceiver;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferType;
import com.orion.visor.module.asset.handler.host.transfer.model.HostConnection;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.visor.module.asset.handler.host.transfer.session.DownloadSession;
import com.orion.visor.module.asset.handler.host.transfer.session.ITransferSession;
import com.orion.visor.module.asset.handler.host.transfer.session.UploadSession;
import com.orion.visor.module.asset.handler.host.transfer.utils.TransferUtils;
import com.orion.visor.module.asset.service.HostTerminalService;
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

    private final WebSocketSession channel;

    private ITransferSession currentSession;

    private final ConcurrentHashMap<String, ITransferSession> sessions;

    private final ConcurrentHashMap<Long, HostConnection> hostConnections;

    public TransferHandler(WebSocketSession channel) {
        this.channel = channel;
        this.sessions = new ConcurrentHashMap<>();
        this.hostConnections = new ConcurrentHashMap<>();
    }

    @Override
    public void handleMessage(TransferOperatorRequest payload) {
        // 解析消息类型
        TransferOperator operator = TransferOperator.of(payload.getOperator());
        // 获取会话
        if (!this.getAndInitSession(payload)) {
            return;
        }
        // 处理消息
        if (TransferOperator.START.equals(operator)) {
            // 开始传输
            currentSession.setToken(UUIds.random32());
            currentSession.onStart(payload);
        } else if (TransferOperator.FINISH.equals(operator)) {
            // 完成
            currentSession.onFinish(payload);
        } else if (TransferOperator.ERROR.equals(operator)) {
            // 失败
            currentSession.onError(payload);
        } else if (TransferOperator.ABORT.equals(operator)) {
            // 中断
            currentSession.onAbort(payload);
        }
    }

    @Override
    public void handleMessage(byte[] content) {
        currentSession.handleBinary(content);
    }

    /**
     * 获取并且初始化会话
     *
     * @param payload payload
     * @return success
     */
    private boolean getAndInitSession(TransferOperatorRequest payload) {
        Long hostId = payload.getHostId();
        TransferType type = TransferType.of(payload.getType());
        String sessionKey = hostId + "_" + type.getType();
        try {
            // 获取主机连接信息
            HostConnection hostConnection = hostConnections.get(hostId);
            if (hostConnection == null) {
                // 获取主机连接信息
                Long userId = WebSockets.getAttr(channel, ExtraFieldConst.USER_ID);
                HostTerminalConnectDTO connectInfo = hostTerminalService.getTerminalConnectInfo(userId, hostId);
                hostConnection = new HostConnection(connectInfo, SessionStores.openSessionStore(connectInfo));
                hostConnections.put(hostId, hostConnection);
            }
            SessionStore sessionStore = hostConnection.getSessionStore();
            HostTerminalConnectDTO connectInfo = hostConnection.getConnectInfo();
            // 获取会话
            ITransferSession session = sessions.get(sessionKey);
            if (session == null) {
                // 打开会话并初始化
                if (TransferType.UPLOAD.equals(type)) {
                    // 上传会话
                    session = new UploadSession(connectInfo, sessionStore, this.channel);
                } else if (TransferType.DOWNLOAD.equals(type)) {
                    // 下载会话
                    session = new DownloadSession(connectInfo, sessionStore, this.channel);
                }
                session.init();
                sessions.put(sessionKey, session);
                log.info("TransferHandler.getAndInitSession success channelId: {}, hostId: {}", channel.getId(), hostId);
            }
            this.currentSession = session;
            return true;
        } catch (Exception e) {
            log.error("TransferHandler.getAndInitSession error channelId: {}", channel.getId(), e);
            // 响应结果
            TransferUtils.sendMessage(this.channel, TransferReceiver.ERROR, e);
            return false;
        }
    }

    @Override
    public ITransferSession getSessionByToken(String token) {
        return sessions.values()
                .stream()
                .filter(s -> token.equals(s.getToken()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void close() {
        log.info("TransferHandler.close channelId: {}", channel.getId());
        sessions.values().forEach(Streams::close);
        sessions.clear();
    }

}
