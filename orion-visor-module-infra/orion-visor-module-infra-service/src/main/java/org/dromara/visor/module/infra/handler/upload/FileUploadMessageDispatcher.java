/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.handler.upload;

import cn.orionsec.kit.lang.annotation.Keep;
import cn.orionsec.kit.lang.utils.io.Streams;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.common.file.FileClient;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.infra.entity.dto.FileUploadTokenDTO;
import org.dromara.visor.module.infra.handler.upload.enums.FileUploadOperatorType;
import org.dromara.visor.module.infra.handler.upload.handler.FileUploadHandler;
import org.dromara.visor.module.infra.handler.upload.handler.IFileUploadHandler;
import org.dromara.visor.module.infra.handler.upload.model.FileUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上传消息处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 18:22
 */
@Slf4j
@Component
public class FileUploadMessageDispatcher extends AbstractWebSocketHandler {

    private final ConcurrentHashMap<String, IFileUploadHandler> handlers = new ConcurrentHashMap<>();

    @Keep
    @Resource
    private FileClient localFileClient;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 获取处理器
        IFileUploadHandler handler = handlers.computeIfAbsent(session.getId(), s -> {
            FileUploadTokenDTO info = WebSockets.getAttr(session, ExtraFieldConst.INFO);
            return new FileUploadHandler(session, localFileClient, info.getEndpoint());
        });
        // 处理消息
        FileUploadRequest request = JSON.parseObject(message.getPayload(), FileUploadRequest.class);
        FileUploadOperatorType type = FileUploadOperatorType.of(request.getType());
        if (FileUploadOperatorType.START.equals(type)) {
            // 开始上传
            handler.start(request.getFileId());
        } else if (FileUploadOperatorType.FINISH.equals(type)) {
            // 上传完成
            handler.finish();
        } else if (FileUploadOperatorType.ERROR.equals(type)) {
            // 上传失败
            handler.error();
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        handlers.get(session.getId()).write(message.getPayload().array());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("FileUploadMessageDispatcher-afterConnectionEstablished id: {}", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("FileUploadMessageDispatcher-handleTransportError id: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        log.info("FileUploadMessageDispatcher-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 关闭会话
        Streams.close(handlers.remove(id));
    }

}
