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
package org.dromara.visor.module.terminal.handler.terminal.handler;

import org.dromara.visor.module.terminal.handler.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ITerminalSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 关闭 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalCloseHandler extends AbstractTerminalHandler<ITerminalSender, TerminalBasePayload> {

    @Override
    public void handle(TerminalChannelProps props, ITerminalSender sender, TerminalBasePayload payload) {
        String sessionId = props.getId();
        log.info("TerminalCloseHandler-handle start sessionId: {}", sessionId);
        // 获取会话
        ITerminalSession session = terminalManager.getSession(sessionId);
        if (session == null) {
            return;
        }
        // 关闭会话
        terminalManager.closeSession(sessionId);
    }

}
