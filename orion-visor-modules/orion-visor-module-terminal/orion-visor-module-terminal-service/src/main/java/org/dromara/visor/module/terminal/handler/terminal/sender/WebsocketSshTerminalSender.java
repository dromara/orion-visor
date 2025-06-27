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

import org.dromara.visor.module.terminal.handler.terminal.enums.OutputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.response.SshOutputResponse;
import org.springframework.web.socket.WebSocketSession;

/**
 * websocket ssh 终端消息发送器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/30 14:33
 */
public class WebsocketSshTerminalSender extends AbstractWebsocketTerminalSender implements ISshTerminalSender {

    public WebsocketSshTerminalSender(WebSocketSession channel) {
        super(channel);
    }

    @Override
    public void sendOutput(String body) {
        this.sendFormattedMessage(OutputProtocolEnum.SSH_OUTPUT,
                SshOutputResponse.builder()
                        .body(body)
                        .build());
    }

}
