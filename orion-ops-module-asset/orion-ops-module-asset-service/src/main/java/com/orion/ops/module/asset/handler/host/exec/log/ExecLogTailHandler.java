package com.orion.ops.module.asset.handler.host.exec.log;

import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.entity.dto.ExecHostLogTailDTO;
import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;
import com.orion.ops.module.asset.handler.host.exec.log.constant.LogConst;
import com.orion.ops.module.asset.handler.host.exec.log.manager.ExecLogManager;
import com.orion.ops.module.asset.handler.host.exec.log.tracker.ExecLogTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.annotation.Resource;

/**
 * 执行日志查看处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 18:38
 */
@Slf4j
@Component
public class ExecLogTailHandler extends AbstractWebSocketHandler {

    @Resource
    private FileClient logsFileClient;

    @Resource
    private ExecLogManager execLogManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String id = session.getId();
        log.info("ExecLogTailHandler-afterConnectionEstablished id: {}", id);
        // 获取参数
        ExecLogTailDTO info = WebSockets.getAttr(session, ExtraFieldConst.INFO);
        // 打开会话
        for (ExecHostLogTailDTO host : info.getHosts()) {
            String trackerId = this.getTrackerId(id, info, host);
            String absolutePath = logsFileClient.getAbsolutePath(host.getPath());
            // 追踪器
            ExecLogTracker tracker = new ExecLogTracker(trackerId, absolutePath, session, host);
            // 执行
            AssetThreadPools.EXEC_LOG.execute(tracker);
            // 添加追踪器
            execLogManager.addTracker(tracker);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        // ping
        if (LogConst.PING_PAYLOAD.equals(payload)) {
            WebSockets.sendText(session, LogConst.PONG_PAYLOAD);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("ExecLogTailHandler-handleTransportError id: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        log.info("ExecLogTailHandler-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 关闭会话
        ExecLogTailDTO info = WebSockets.getAttr(session, ExtraFieldConst.INFO);
        // 移除追踪器
        for (ExecHostLogTailDTO host : info.getHosts()) {
            execLogManager.removeTracker(this.getTrackerId(id, info, host));
        }
    }

    /**
     * 获取追踪器 id
     *
     * @param id   id
     * @param info info
     * @param host host
     * @return trackerId
     */
    private String getTrackerId(String id, ExecLogTailDTO info, ExecHostLogTailDTO host) {
        return id + "_" + info.getId() + "_" + host.getId();
    }

}
