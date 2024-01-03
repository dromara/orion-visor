package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.exception.ConnectionRuntimeException;
import com.orion.lang.exception.TimeoutException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.handler.host.terminal.constant.TerminalMessage;
import com.orion.ops.module.asset.handler.host.terminal.entity.Message;
import com.orion.ops.module.asset.handler.host.terminal.entity.request.TerminalConnectRequest;
import com.orion.ops.module.asset.handler.host.terminal.entity.response.TerminalConnectResponse;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputOperatorTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.session.TerminalSession;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.ops.module.asset.service.HostTerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

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

    @Resource
    private TerminalManager terminalManager;

    public TerminalConnectHandler() {
        super(TerminalConnectRequest.class);
    }

    @Override
    protected void handle(WebSocketSession session, Message<TerminalConnectRequest> msg) {
        String token = msg.getSession();
        log.info("TerminalConnectHandler-handle start token: {}", token);
        // 获取主机连接信息
        HostTerminalConnectDTO connect = this.getAttr(session, token);
        if (connect == null) {
            log.info("TerminalConnectHandler-handle unknown token: {}", token);
            this.send(session, msg,
                    OutputOperatorTypeEnum.CONNECT,
                    new TerminalConnectResponse(BooleanBit.FALSE.getValue(), ErrorMessage.SESSION_ABSENT));
            return;
        }
        // 移除会话连接信息
        session.getAttributes().remove(token);
        Exception ex = null;
        try {
            // 连接主机
            TerminalSession terminalSession = this.connect(token, connect, session, msg.getBody());
            // 添加会话到 manager
            terminalManager.addSession(terminalSession);
        } catch (Exception e) {
            ex = e;
            // 修改连接状态为失败
            hostConnectLogService.updateStatusByToken(token, HostConnectStatusEnum.FAILED);
        }
        // 返回连接状态
        this.send(session, msg,
                OutputOperatorTypeEnum.CONNECT,
                TerminalConnectResponse.builder()
                        .result(BooleanBit.of(ex == null).getValue())
                        .errorMessage(this.getConnectErrorMessage(ex))
                        .build());
    }

    /**
     * 连接主机
     *
     * @param token   token
     * @param connect connect
     * @param session session
     * @param body    body
     * @return session
     */
    private TerminalSession connect(String token,
                                    HostTerminalConnectDTO connect,
                                    WebSocketSession session,
                                    TerminalConnectRequest body) {
        TerminalSession terminalSession = null;
        try {
            // 建立连接
            SessionStore sessionStore = hostTerminalService.openSessionStore(connect);
            terminalSession = new TerminalSession(token, session, sessionStore);
            terminalSession.connect(body.getCols(), body.getRows());
            log.info("TerminalConnectHandler-handle success token: {}", token);
            return terminalSession;
        } catch (Exception e) {
            Streams.close(terminalSession);
            log.error("TerminalConnectHandler-handle error token: {}", token, e);
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
        } else if (Exceptions.isCausedBy(e, ConnectionRuntimeException.class)) {
            // 无法连接
            return TerminalMessage.UNREACHABLE;
        } else if (Exceptions.isCausedBy(e, AuthenticationException.class)) {
            // 认证失败
            return TerminalMessage.AUTHENTICATION_FAILURE;
        } else if (Exceptions.isCausedBy(e, InvalidArgumentException.class)) {
            // 参数错误
            return e.getMessage();
        } else {
            // 其他错误
            return TerminalMessage.UNREACHABLE;
        }
    }

}
