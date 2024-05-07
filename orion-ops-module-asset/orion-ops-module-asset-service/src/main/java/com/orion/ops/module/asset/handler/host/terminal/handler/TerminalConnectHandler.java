package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.exception.TimeoutException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.constant.TerminalMessage;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.ops.module.asset.handler.host.terminal.model.request.TerminalConnectRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.TerminalConnectResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.ops.module.asset.handler.host.terminal.session.SftpSession;
import com.orion.ops.module.asset.handler.host.terminal.session.SshSession;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.ops.module.asset.service.HostTerminalService;
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
    private HostTerminalService hostTerminalService;

    @Resource
    private HostConnectLogService hostConnectLogService;

    @Override
    public void handle(WebSocketSession channel, TerminalConnectRequest payload) {
        String sessionId = payload.getSessionId();
        log.info("TerminalConnectHandler-handle start sessionId: {}", sessionId);
        // 获取主机连接信息
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
        try {
            // 连接主机
            ITerminalSession session = this.connect(sessionId, connect, channel, payload);
            // 添加会话到 manager
            terminalManager.addSession(session);
        } catch (Exception e) {
            ex = e;
            // 修改连接状态为失败
            Map<String, Object> extra = Maps.newMap(4);
            extra.put(ExtraFieldConst.ERROR_MESSAGE, this.getConnectErrorMessage(e));
            hostConnectLogService.updateStatusById(connect.getLogId(), HostConnectStatusEnum.FAILED, extra);
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
            SessionStore sessionStore = hostTerminalService.openSessionStore(connect);
            if (HostConnectTypeEnum.SSH.name().equals(connectType)) {
                // 打开 ssh 会话
                SshSession sshSession = new SshSession(sessionId, channel, sessionStore, config);
                sshSession.connect(body.getTerminalType(), body.getCols(), body.getRows());
                session = sshSession;
            } else if (HostConnectTypeEnum.SFTP.name().equals(connectType)) {
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
