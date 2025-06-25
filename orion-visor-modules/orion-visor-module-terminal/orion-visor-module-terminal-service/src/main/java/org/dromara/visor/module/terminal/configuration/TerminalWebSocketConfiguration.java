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
package org.dromara.visor.module.terminal.configuration;

import org.dromara.visor.module.terminal.handler.terminal.TerminalAccessRdpHandler;
import org.dromara.visor.module.terminal.handler.terminal.TerminalAccessSftpHandler;
import org.dromara.visor.module.terminal.handler.terminal.TerminalAccessSshHandler;
import org.dromara.visor.module.terminal.handler.transfer.TransferMessageDispatcher;
import org.dromara.visor.module.terminal.interceptor.TerminalAccessInterceptor;
import org.dromara.visor.module.terminal.interceptor.TerminalTransferInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 终端模块 websocket 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 11:39
 */
@Configuration
public class TerminalWebSocketConfiguration implements WebSocketConfigurer {

    @Value("${orion.websocket.prefix}")
    private String prefix;

    @Resource
    private TerminalAccessInterceptor terminalAccessInterceptor;

    @Resource
    private TerminalTransferInterceptor terminalTransferInterceptor;

    @Resource
    private TerminalAccessSshHandler terminalAccessSshHandler;

    @Resource
    private TerminalAccessSftpHandler terminalAccessSftpHandler;

    @Resource
    private TerminalAccessRdpHandler terminalAccessRdpHandler;

    @Resource
    private TransferMessageDispatcher transferMessageDispatcher;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // SSH 终端会话
        registry.addHandler(terminalAccessSshHandler, prefix + "/terminal/access/ssh/{accessToken}")
                .addInterceptors(terminalAccessInterceptor)
                .setAllowedOrigins("*");
        // SFTP 终端会话
        registry.addHandler(terminalAccessSftpHandler, prefix + "/terminal/access/sftp/{accessToken}")
                .addInterceptors(terminalAccessInterceptor)
                .setAllowedOrigins("*");
        // RDP 终端会话
        registry.addHandler(terminalAccessRdpHandler, prefix + "/terminal/access/rdp/{accessToken}")
                .addInterceptors(terminalAccessInterceptor)
                .setAllowedOrigins("*");
        // 文件传输
        registry.addHandler(transferMessageDispatcher, prefix + "/terminal/transfer/{transferToken}")
                .addInterceptors(terminalTransferInterceptor)
                .setAllowedOrigins("*");
    }

}
