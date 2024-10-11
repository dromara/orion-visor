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
package com.orion.visor.module.asset.handler.host.transfer;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.io.Streams;
import com.orion.visor.module.asset.handler.host.transfer.handler.ITransferHandler;
import com.orion.visor.module.asset.handler.host.transfer.handler.TransferHandler;
import com.orion.visor.module.asset.handler.host.transfer.manager.HostTransferManager;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;

/**
 * sftp 传输消息处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 18:22
 */
@Slf4j
@Component
public class TransferMessageDispatcher extends AbstractWebSocketHandler {

    @Resource
    private HostTransferManager hostTransferManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("TransferMessageHandler-afterConnectionEstablished id: {}", session.getId());
        // 添加处理器
        hostTransferManager.putHandler(session.getId(), new TransferHandler(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 获取处理器
        ITransferHandler handler = hostTransferManager.getHandler(session.getId());
        // 处理消息
        handler.handleMessage(JSON.parseObject(message.getPayload(), TransferOperatorRequest.class));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        // 获取处理器
        ITransferHandler handler = hostTransferManager.getHandler(session.getId());
        // 添加数据
        handler.handleMessage(message.getPayload().array());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("TransferMessageHandler-handleTransportError id: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        log.info("TransferMessageHandler-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 关闭会话
        Streams.close(hostTransferManager.removeHandler(id));
    }

}
