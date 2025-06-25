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
package org.dromara.visor.module.exec.handler.exec.log;

import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.exec.define.ExecThreadPools;
import org.dromara.visor.module.exec.entity.dto.ExecLogTailDTO;
import org.dromara.visor.module.exec.handler.exec.log.constant.LogConst;
import org.dromara.visor.module.exec.handler.exec.log.manager.ExecLogManager;
import org.dromara.visor.module.exec.handler.exec.log.tracker.ExecLogTracker;
import cn.orionsec.kit.lang.utils.Strings;
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
    private ExecLogManager execLogManager;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String id = session.getId();
        String payload = message.getPayload();
        if (LogConst.PING_PAYLOAD.equals(payload)) {
            // ping
            WebSockets.sendText(session, LogConst.PONG_PAYLOAD);
        } else if (Strings.isInteger(payload)) {
            // 获取日志
            ExecLogTailDTO info = WebSockets.getAttr(session, ExtraFieldConst.INFO);
            Long execHostId = Long.valueOf(payload);
            ExecLogTracker tracker = new ExecLogTracker(info.getExecId(),
                    execHostId,
                    WebSockets.createSyncSession(session));
            // 执行追踪器
            ExecThreadPools.EXEC_LOG.execute(tracker);
            // 添加追踪器
            execLogManager.addTracker(id, tracker);
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
        // 移除追踪器
        execLogManager.removeTrackers(id);
    }

}
