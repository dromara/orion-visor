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

import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.request.GuacdInstructionRequest;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.IRdpSession;
import org.dromara.visor.module.terminal.handler.terminal.session.ITerminalSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * guacd 指令处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class GuacdInstructionHandler extends AbstractTerminalHandler<IGuacdTerminalSender, GuacdInstructionRequest> {

    @Override
    public void handle(TerminalChannelProps props, IGuacdTerminalSender sender, GuacdInstructionRequest payload) {
        // 获取会话
        ITerminalSession session = terminalManager.getSession(props.getId());
        if (session instanceof IRdpSession) {
            // 处理 rdp 指令
            this.processRdpInstruction((IRdpSession) session, payload.getInstruction());
        }
    }

    /**
     * 处理 rdp 指令
     *
     * @param session     session
     * @param instruction instruction
     */
    private void processRdpInstruction(IRdpSession session, String instruction) {
        session.write(instruction);
    }

}
