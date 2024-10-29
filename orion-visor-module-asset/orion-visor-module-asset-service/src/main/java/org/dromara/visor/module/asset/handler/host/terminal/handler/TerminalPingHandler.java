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
package org.dromara.visor.module.asset.handler.host.terminal.handler;

import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.asset.handler.host.terminal.session.ITerminalSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * ping 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalPingHandler extends AbstractTerminalHandler<TerminalBasePayload> {

    @Override
    public void handle(WebSocketSession channel, TerminalBasePayload payload) {
        // 发送 pong
        this.send(channel, OutputTypeEnum.PONG.getType());
        // 活跃 terminal
        Map<String, ITerminalSession> sessions = terminalManager.getSession(channel.getId());
        if (!Maps.isEmpty(sessions)) {
            for (ITerminalSession session : sessions.values()) {
                session.keepAlive();
            }
        }
    }

}
