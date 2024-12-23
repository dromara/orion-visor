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
package org.dromara.visor.module.asset.handler.host.terminal.handler;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.asset.handler.host.terminal.model.request.SshInputRequest;
import org.dromara.visor.module.asset.handler.host.terminal.session.ISshSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * ssh 处理输入处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class SshInputHandler extends AbstractTerminalHandler<SshInputRequest> {

    @Override
    public void handle(WebSocketSession channel, SshInputRequest payload) {
        // 获取会话
        ISshSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        // 处理输入
        session.write(payload.getCommand());
    }

}
