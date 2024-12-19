/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.handler.host.terminal;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.define.AssetThreadPools;
import org.dromara.visor.module.asset.handler.host.terminal.enums.InputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.manager.TerminalManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

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
public class TerminalMessageDispatcher extends AbstractWebSocketHandler {

    @Resource
    private TerminalManager terminalManager;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try {
            // 解析类型
            InputTypeEnum type = InputTypeEnum.of(payload);
            if (type == null) {
                return;
            }
            // 解析并处理消息
            if (type.isAsyncExec()) {
                // 异步执行
                AssetThreadPools.TERMINAL_OPERATOR.execute(() -> {
                    type.getHandler().handle(session, type.parse(payload));
                });
            } else {
                // 同步执行
                type.getHandler().handle(session, type.parse(payload));
            }
        } catch (Exception e) {
            log.error("TerminalDispatchHandler-handleMessage-error id: {}, msg: {}", session.getId(), payload, e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("TerminalMessageDispatcher-afterConnectionEstablished id: {}", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("TerminalMessageDispatcher-handleTransportError id: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        log.info("TerminalMessageDispatcher-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 关闭会话
        terminalManager.closeSession(id);
    }

}
