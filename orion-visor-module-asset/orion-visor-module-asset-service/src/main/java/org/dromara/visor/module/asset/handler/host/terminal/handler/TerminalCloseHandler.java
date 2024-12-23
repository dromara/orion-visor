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
package org.dromara.visor.module.asset.handler.host.terminal.handler;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 关闭处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalCloseHandler extends AbstractTerminalHandler<TerminalBasePayload> {

    @Override
    public void handle(WebSocketSession channel, TerminalBasePayload payload) {
        log.info("TerminalCloseHandler-handle start sessionId: {}", payload.getSessionId());
        // 关闭会话
        terminalManager.closeSession(channel.getId(), payload.getSessionId());
    }

}
