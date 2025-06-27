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
import org.dromara.visor.module.common.config.AppSftpConfig;
import org.dromara.visor.module.terminal.handler.terminal.enums.InputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.sender.WebsocketSftpTerminalSender;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * sftp 终端处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 14:33
 */
@Slf4j
@Component
public class TerminalAccessSftpHandler extends AbstractTerminalAccessHandler<ISftpTerminalSender> {

    @Resource
    private AppSftpConfig appSftpConfig;

    @Override
    protected ISftpTerminalSender createSender(WebSocketSession channel) {
        return new WebsocketSftpTerminalSender(channel);
    }

    @Override
    protected void initSession(WebSocketSession channel, TerminalChannelProps props) {
        // 预览大小
        props.getExtra().setFilePreviewSize(appSftpConfig.getPreviewSize());
    }

    @Override
    protected void handleMessage(WebSocketSession channel, TextMessage message, TerminalChannelProps props, ISftpTerminalSender sender) {
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
            log.error("TerminalAccessSftpHandler-handleMessage-error id: {}, msg: {}", channel.getId(), payload, e);
        }
    }

}
