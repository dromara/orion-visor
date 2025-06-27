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
package org.dromara.visor.module.terminal.handler.terminal.sender;

import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.terminal.handler.terminal.enums.OutputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.terminal.handler.terminal.model.response.SshResizeResponse;
import org.dromara.visor.module.terminal.handler.terminal.model.response.TerminalClosedResponse;
import org.dromara.visor.module.terminal.handler.terminal.model.response.TerminalSetIdResponse;
import org.dromara.visor.module.terminal.handler.terminal.model.response.TerminalSetInfoResponse;
import org.springframework.web.socket.WebSocketSession;

/**
 * websocket 终端 发送器基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/4/1 10:43
 */
public abstract class AbstractWebsocketTerminalSender implements ITerminalSender {

    protected boolean closed;

    protected final WebSocketSession channel;

    public AbstractWebsocketTerminalSender(WebSocketSession channel) {
        this.channel = channel;
    }

    @Override
    public void sendMessage(String body) {
        this.send(channel, body);
    }

    @Override
    public void sendSetId(String sessionId) {
        this.sendFormattedMessage(OutputProtocolEnum.SET_ID,
                TerminalSetIdResponse.builder()
                        .sessionId(sessionId)
                        .build());
    }

    @Override
    public void sendSetInfo(String info) {
        this.sendFormattedMessage(OutputProtocolEnum.SET_INFO,
                TerminalSetInfoResponse.builder()
                        .info(info)
                        .build());
    }

    @Override
    public void sendResize(int width, int height) {
        this.sendFormattedMessage(OutputProtocolEnum.RESIZE,
                SshResizeResponse.builder()
                        .width(width)
                        .height(height)
                        .build());
    }

    @Override
    public void sendPong() {
        this.send(channel, OutputProtocolEnum.PONG.getType());
    }

    @Override
    public void sendConnected() {
        this.send(channel, OutputProtocolEnum.CONNECTED.getType());
    }

    @Override
    public void sendClosed(int code, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.CLOSED,
                TerminalClosedResponse.builder()
                        .code(code)
                        .msg(message)
                        .build());
    }

    /**
     * 发送消息
     *
     * @param type type
     * @param body body
     * @param <E>  E
     */
    protected <E extends TerminalBasePayload> void sendFormattedMessage(OutputProtocolEnum type, E body) {
        body.setType(type.getType());
        // 发送消息
        this.send(channel, type.format(body));
    }

    /**
     * 发送消息
     *
     * @param channel channel
     * @param message message
     */
    protected void send(WebSocketSession channel, String message) {
        if (this.isOpen()) {
            WebSockets.sendText(channel, message);
        }
    }

    @Override
    public void setClosed() {
        this.closed = true;
    }

    @Override
    public boolean isOpen() {
        return !closed && channel.isOpen();
    }

    @Override
    public void close() {
        WebSockets.close(channel);
    }

}
