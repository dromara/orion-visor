package com.orion.ops.module.asset.handler.host.terminal;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.websocket.core.handler.TextWebSocketHandler;
import com.orion.ops.module.asset.handler.host.terminal.entity.MessageWrapper;
import com.orion.ops.module.asset.handler.host.terminal.enums.InputOperatorTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.handler.TerminalConnectHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Optional;

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

    @Resource
    private TerminalConnectHandler terminalConnectHandler;

    @Override
    public void onMessage(WebSocketSession session, String payload) {
        try {
            // 解析类型
            InputOperatorTypeEnum type = Optional.ofNullable(payload)
                    .map(s -> JSON.parseObject(s, MessageWrapper.class))
                    .map(MessageWrapper::getType)
                    .map(InputOperatorTypeEnum::of)
                    .orElse(null);
            if (InputOperatorTypeEnum.CONNECT.equals(type)) {
                // 连接主机
                // {"t":"co","s": "1001","b":{"h":1}}
                terminalConnectHandler.process(session, payload);
            } else if (InputOperatorTypeEnum.CLOSE.equals(type)) {
                // 关闭连接

            } else if (InputOperatorTypeEnum.PING.equals(type)) {
                // ping

            } else if (InputOperatorTypeEnum.RESIZE.equals(type)) {
                // resize

            } else if (InputOperatorTypeEnum.EXEC.equals(type)) {
                // 执行

            } else if (InputOperatorTypeEnum.INPUT.equals(type)) {
                // 输入

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
