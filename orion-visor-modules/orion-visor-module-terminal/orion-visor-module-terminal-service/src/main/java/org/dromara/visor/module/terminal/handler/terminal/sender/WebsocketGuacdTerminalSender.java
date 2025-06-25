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
package org.dromara.visor.module.terminal.handler.terminal.sender;

import org.apache.guacamole.protocol.GuacamoleInstruction;
import org.dromara.visor.module.terminal.handler.terminal.enums.OutputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.response.GuacdInstructionResponse;
import org.springframework.web.socket.WebSocketSession;

/**
 * websocket guacd 发送器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/4/1 10:38
 */
public class WebsocketGuacdTerminalSender extends AbstractWebsocketTerminalSender implements IGuacdTerminalSender {

    public WebsocketGuacdTerminalSender(WebSocketSession channel) {
        super(channel);
    }

    @Override
    public void sendConnected() {
        // guacd 不需要发送 connected 指令, 是通过 guacd 内部消息监听器判断是否已连接
        // this.send(channel, OutputProtocolEnum.CONNECTED.getType());
    }

    @Override
    public void sendInstruction(GuacamoleInstruction instruction) {
        this.sendInstruction(instruction.toString());
    }

    @Override
    public void sendInstruction(String instruction) {
        this.sendFormattedMessage(OutputProtocolEnum.GUACD_INSTRUCTION,
                GuacdInstructionResponse.builder()
                        .instruction(instruction)
                        .build());
    }

}
