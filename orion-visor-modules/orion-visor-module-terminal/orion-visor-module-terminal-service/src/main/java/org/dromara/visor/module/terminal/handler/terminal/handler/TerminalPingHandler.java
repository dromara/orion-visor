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

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.terminal.handler.terminal.enums.SessionTypeEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ITerminalSession;
import org.springframework.stereotype.Component;

/**
 * ping 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalPingHandler extends AbstractTerminalHandler<ITerminalSender, TerminalBasePayload> {

    @Override
    public void handle(TerminalChannelProps props, ITerminalSender sender, TerminalBasePayload payload) {
        if (SessionTypeEnum.TERMINAL.name().equals(props.getType())) {
            // 活跃 terminal
            ITerminalSession session = terminalManager.getSession(props.getId());
            if (session != null) {
                session.keepAlive();
            }
        }
        // 发送 pong
        sender.sendPong();
    }

}
