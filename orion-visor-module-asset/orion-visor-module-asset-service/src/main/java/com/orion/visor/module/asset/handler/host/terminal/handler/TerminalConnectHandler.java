/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.handler.host.terminal.handler;

import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.exception.TimeoutException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.common.enums.BooleanBit;
import com.orion.visor.framework.websocket.core.utils.WebSockets;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.enums.TerminalConnectStatusEnum;
import com.orion.visor.module.asset.enums.TerminalConnectTypeEnum;
import com.orion.visor.module.asset.handler.host.jsch.SessionStores;
import com.orion.visor.module.asset.handler.host.terminal.constant.TerminalMessage;
import com.orion.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.visor.module.asset.handler.host.terminal.model.request.TerminalConnectRequest;
import com.orion.visor.module.asset.handler.host.terminal.model.response.TerminalConnectResponse;
import com.orion.visor.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.visor.module.asset.handler.host.terminal.session.SftpSession;
import com.orion.visor.module.asset.handler.host.terminal.session.SshSession;
import com.orion.visor.module.asset.service.TerminalConnectLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 连接主机处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalConnectHandler extends AbstractTerminalHandler<TerminalConnectRequest> {

    @Resource
    private TerminalConnectLogService terminalConnectLogService;

    @Override
    public void handle(WebSocketSession channel, TerminalConnectRequest payload) {
        String sessionId = payload.getSessionId();
        log.info("TerminalConnectHandler-handle start sessionId: {}", sessionId);
        // 获取终端连接信息
        HostTerminalConnectDTO connect = WebSockets.getAttr(channel, sessionId);
        if (connect == null) {
            log.info("TerminalConnectHandler-handle unknown sessionId: {}", sessionId);
            this.send(channel,
                    OutputTypeEnum.CONNECT,
                    TerminalConnectResponse.builder()
                            .sessionId(payload.getSessionId())
                            .result(BooleanBit.FALSE.getValue())
                            .msg(ErrorMessage.SESSION_ABSENT)
                            .build());
            return;
        }
        // 移除会话连接信息
        channel.getAttributes().remove(sessionId);
        Exception ex = null;
        ITerminalSession session = null;
        try {
            // 连接主机
            session = this.connect(sessionId, connect, channel, payload);
            // 添加会话到 manager
            hostTerminalManager.addSession(session);
        } catch (Exception e) {
            ex = e;
            Streams.close(session);
            // 修改连接状态为失败
            Map<String, Object> extra = Maps.newMap(4);
            extra.put(ExtraFieldConst.ERROR_MESSAGE, this.getConnectErrorMessage(e));
            terminalConnectLogService.updateStatusById(connect.getLogId(), TerminalConnectStatusEnum.FAILED, extra);
        }
        // 返回连接状态
        this.send(channel,
                OutputTypeEnum.CONNECT,
                TerminalConnectResponse.builder()
                        .sessionId(payload.getSessionId())
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getConnectErrorMessage(ex))
                        .build());
    }

    /**
     * 连接主机
     *
     * @param sessionId sessionId
     * @param connect   connect
     * @param channel   channel
     * @param body      body
     * @return channel
     */
    private ITerminalSession connect(String sessionId,
                                     HostTerminalConnectDTO connect,
                                     WebSocketSession channel,
                                     TerminalConnectRequest body) {
        String connectType = connect.getConnectType();
        ITerminalSession session = null;
        try {
            // 连接配置
            TerminalConfig config = TerminalConfig.builder()
                    .logId(connect.getLogId())
                    .hostId(connect.getHostId())
                    .hostName(connect.getHostName())
                    .address(connect.getHostAddress())
                    .charset(connect.getCharset())
                    .fileNameCharset(connect.getFileNameCharset())
                    .fileContentCharset(connect.getFileContentCharset())
                    .build();
            // 建立连接
            SessionStore sessionStore = SessionStores.openSessionStore(connect);
            if (TerminalConnectTypeEnum.SSH.name().equals(connectType)) {
                // 打开 ssh 会话
                SshSession sshSession = new SshSession(sessionId, channel, sessionStore, config);
                sshSession.connect(body.getTerminalType(), body.getCols(), body.getRows());
                session = sshSession;
            } else if (TerminalConnectTypeEnum.SFTP.name().equals(connectType)) {
                // 打开 sftp 会话
                SftpSession sftpSession = new SftpSession(sessionId, channel, sessionStore, config);
                sftpSession.connect();
                session = sftpSession;
            }
            log.info("TerminalConnectHandler-handle success sessionId: {}", sessionId);
            return session;
        } catch (Exception e) {
            Streams.close(session);
            log.error("TerminalConnectHandler-handle error sessionId: {}", sessionId, e);
            throw e;
        }
    }

    /**
     * 获取建立连接错误信息
     *
     * @param e e
     * @return errorMessage
     */
    private String getConnectErrorMessage(Exception e) {
        if (e == null) {
            return null;
        }
        if (Exceptions.isCausedBy(e, TimeoutException.class)) {
            // 连接超时
            return TerminalMessage.CONNECTION_TIMEOUT;
        } else if (Exceptions.isCausedBy(e, AuthenticationException.class)) {
            // 认证失败
            return TerminalMessage.AUTHENTICATION_FAILURE;
        } else if (Exceptions.isCausedBy(e, InvalidArgumentException.class)) {
            // 参数错误
            return e.getMessage();
        } else {
            // 其他错误
            return TerminalMessage.SERVER_UNREACHABLE;
        }
    }

}
