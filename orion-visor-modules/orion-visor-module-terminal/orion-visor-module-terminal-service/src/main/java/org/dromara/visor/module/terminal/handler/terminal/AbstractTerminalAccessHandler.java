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
package org.dromara.visor.module.terminal.handler.terminal;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.FieldConst;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.terminal.handler.terminal.manager.TerminalManager;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 终端处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 14:33
 */
@Slf4j
@Component
public abstract class AbstractTerminalAccessHandler<T extends ITerminalSender> extends AbstractWebSocketHandler {

    @Resource
    protected TerminalManager terminalManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession channel) {
        String id = channel.getId();
        log.info("AbstractTerminalAccessHandler-afterConnectionEstablished id: {}", id);
        Map<String, Object> attr = channel.getAttributes();
        // 设置消息发送器
        T sender = this.createSender(channel);
        attr.put(FieldConst.SENDER, sender);
        // 设置 sessionId
        TerminalChannelProps props = WebSockets.getAttr(channel, FieldConst.PROPS);
        props.setId(id);
        // 发送 sessionId
        sender.sendSetId(id);
        // 初始化会话
        this.initSession(channel, props);
    }

    /**
     * 创建 sender
     *
     * @param channel sender
     * @return sender
     */
    protected abstract T createSender(WebSocketSession channel);

    /**
     * 初始化会话
     *
     * @param channel channel
     * @param props   props
     */
    protected void initSession(WebSocketSession channel, TerminalChannelProps props) {
    }

    /**
     * 处理消息
     *
     * @param channel channel
     * @param message message
     * @param props   props
     * @param sender  sender
     */
    protected abstract void handleMessage(WebSocketSession channel,
                                          TextMessage message,
                                          TerminalChannelProps props,
                                          T sender);

    @Override
    protected void handleTextMessage(WebSocketSession channel, TextMessage message) {
        // 包装处理消息
        this.handleMessage(channel, message, this.getProps(channel), this.getSender(channel));
    }

    @Override
    public void handleTransportError(WebSocketSession channel, Throwable exception) {
        log.error("AbstractTerminalAccessHandler-handleTransportError id: {}", channel.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession channel, CloseStatus status) {
        String id = channel.getId();
        log.info("AbstractTerminalAccessHandler-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 设置 sender 为已关闭
        T sender = this.getSender(channel);
        if (sender != null) {
            sender.setClosed();
        }
        // 关闭会话
        terminalManager.closeSession(id);
    }

    /**
     * 获取 sender
     *
     * @param channel channel
     * @return sender
     */
    protected T getSender(WebSocketSession channel) {
        return WebSockets.getAttr(channel, FieldConst.SENDER);
    }

    /**
     * 获取 props
     *
     * @param channel channel
     * @return props
     */
    protected TerminalChannelProps getProps(WebSocketSession channel) {
        return WebSockets.getAttr(channel, FieldConst.PROPS);
    }

}
