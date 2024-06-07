package com.orion.visor.module.infra.handler.upload;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.io.Streams;
import com.orion.visor.framework.common.annotation.Keep;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.framework.websocket.core.utils.WebSockets;
import com.orion.visor.module.infra.entity.dto.FileUploadTokenDTO;
import com.orion.visor.module.infra.handler.upload.enums.FileUploadOperatorType;
import com.orion.visor.module.infra.handler.upload.handler.FileUploadHandler;
import com.orion.visor.module.infra.handler.upload.handler.IFileUploadHandler;
import com.orion.visor.module.infra.handler.upload.model.FileUploadRequest;
import lombok.extern.slf4j.Slf4j;
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
