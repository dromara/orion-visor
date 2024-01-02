package com.orion.ops.module.asset.handler.host.terminal;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.websocket.core.handler.TextWebSocketHandler;
import com.orion.ops.module.asset.handler.host.terminal.entity.Message;
import com.orion.ops.module.asset.handler.host.terminal.enums.InputOperatorTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 14:33
 */
@Slf4j
@Component
public class TerminalMessageDispatcher extends TextWebSocketHandler {

    @Override
    public void onMessage(WebSocketSession session, String payload) {
        try {
            // 解析类型
            Message<?> message = JSON.parseObject(payload, Message.class);
            InputOperatorTypeEnum type = InputOperatorTypeEnum.of(message.getType());
            if (type != null) {
                // 处理消息
                type.getHandler().process(session, message);
            }
        } catch (Exception e) {
            log.error("TerminalDispatchHandler-handleMessage-error msg: {}", payload, e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("afterConnectionClosed");
        // release session
    }

}
