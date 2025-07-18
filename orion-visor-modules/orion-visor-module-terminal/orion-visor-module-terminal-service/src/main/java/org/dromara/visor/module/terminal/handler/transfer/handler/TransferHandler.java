/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.terminal.handler.transfer.handler;

import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.module.asset.api.HostConnectApi;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.transfer.enums.TransferOperator;
import org.dromara.visor.module.terminal.handler.transfer.enums.TransferReceiver;
import org.dromara.visor.module.terminal.handler.transfer.enums.TransferType;
import org.dromara.visor.module.terminal.handler.transfer.model.TerminalConnection;
import org.dromara.visor.module.terminal.handler.transfer.model.TransferOperatorRequest;
import org.dromara.visor.module.terminal.handler.transfer.session.DownloadSession;
import org.dromara.visor.module.terminal.handler.transfer.session.ITransferSession;
import org.dromara.visor.module.terminal.handler.transfer.session.UploadSession;
import org.dromara.visor.module.terminal.handler.transfer.utils.TransferUtils;
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

    private static final HostConnectApi hostConnectApi = SpringHolder.getBean(HostConnectApi.class);

    private final TerminalChannelProps props;

    private final WebSocketSession channel;

    private ITransferSession currentSession;

    private final ConcurrentHashMap<String, ITransferSession> sessions;

    private final ConcurrentHashMap<Long, TerminalConnection> connectionMap;

    public TransferHandler(TerminalChannelProps props, WebSocketSession channel) {
        this.props = props;
        this.channel = channel;
        this.sessions = new ConcurrentHashMap<>();
        this.connectionMap = new ConcurrentHashMap<>();
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
            // 获取终端连接信息
            TerminalConnection connection = connectionMap.get(hostId);
            if (connection == null) {
                // 获取终端连接信息
                SshConnectConfig connectConfig = hostConnectApi.getSshConnectConfig(hostId, props.getUserId());
                connection = new TerminalConnection(connectConfig, SessionStores.openSessionStore(connectConfig));
                connectionMap.put(hostId, connection);
            }
            SessionStore sessionStore = connection.getSessionStore();
            SshConnectConfig connectConfig = connection.getConnectConfig();
            // 获取会话
            ITransferSession session = sessions.get(sessionKey);
            if (session == null) {
                // 打开会话并初始化
                if (TransferType.UPLOAD.equals(type)) {
                    // 上传会话
                    session = new UploadSession(connectConfig, sessionStore, this.channel);
                } else if (TransferType.DOWNLOAD.equals(type)) {
                    // 下载会话
                    session = new DownloadSession(connectConfig, sessionStore, this.channel);
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
