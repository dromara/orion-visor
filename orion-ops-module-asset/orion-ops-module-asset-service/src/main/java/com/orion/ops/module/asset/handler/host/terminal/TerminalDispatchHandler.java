package com.orion.ops.module.asset.handler.host.terminal;

import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * 终端处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 14:33
 */
@Slf4j
@Component
public class TerminalDispatchHandler implements WebSocketHandler {

    @Resource
    private OperatorLogFrameworkService operatorLogFrameworkService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("afterConnectionEstablished");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("handleMessage");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("afterConnectionClosed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
