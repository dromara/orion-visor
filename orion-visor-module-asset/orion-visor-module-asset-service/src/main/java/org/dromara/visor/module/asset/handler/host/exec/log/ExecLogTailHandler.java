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
package org.dromara.visor.module.asset.handler.host.exec.log;

import cn.orionsec.kit.lang.annotation.Keep;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.common.file.FileClient;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.asset.define.AssetThreadPools;
import org.dromara.visor.module.asset.entity.dto.ExecHostLogTailDTO;
import org.dromara.visor.module.asset.entity.dto.ExecLogTailDTO;
import org.dromara.visor.module.asset.handler.host.exec.log.constant.LogConst;
import org.dromara.visor.module.asset.handler.host.exec.log.manager.ExecLogManager;
import org.dromara.visor.module.asset.handler.host.exec.log.tracker.ExecLogTracker;
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

    @Keep
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
            ExecLogTracker tracker = new ExecLogTracker(trackerId,
                    absolutePath,
                    WebSockets.createSyncSession(session),
                    host);
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
