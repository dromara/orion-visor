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
package org.dromara.visor.module.terminal.handler.terminal;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.terminal.handler.terminal.enums.InputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.sender.WebsocketGuacdTerminalSender;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * RDP 终端处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 14:33
 */
@Slf4j
@Component
public class TerminalAccessRdpHandler extends AbstractTerminalAccessHandler<IGuacdTerminalSender> {

    @Override
    protected IGuacdTerminalSender createSender(WebSocketSession channel) {
        return new WebsocketGuacdTerminalSender(channel);
    }

    @Override
    protected void handleMessage(WebSocketSession channel, TextMessage message, TerminalChannelProps props, IGuacdTerminalSender sender) {
        String payload = message.getPayload();
        try {
            // 解析类型
            InputProtocolEnum type = InputProtocolEnum.of(payload);
            if (type == null) {
                return;
            }
            // 解析并处理消息
            type.getHandler().handle(props, sender, type.parse(payload));
        } catch (Exception e) {
            log.error("TerminalAccessRdpHandler-handleMessage-error id: {}, msg: {}", channel.getId(), payload, e);
        }
    }

}
