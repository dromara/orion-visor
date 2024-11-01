/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.configuration;

import org.dromara.visor.module.asset.handler.host.exec.log.ExecLogTailHandler;
import org.dromara.visor.module.asset.handler.host.terminal.TerminalMessageDispatcher;
import org.dromara.visor.module.asset.handler.host.transfer.TransferMessageDispatcher;
import org.dromara.visor.module.asset.interceptor.ExecLogTailInterceptor;
import org.dromara.visor.module.asset.interceptor.TerminalAccessInterceptor;
import org.dromara.visor.module.asset.interceptor.TerminalTransferInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 资产模块 websocket 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 11:39
 */
@Configuration
public class AssetWebSocketConfiguration implements WebSocketConfigurer {

    @Value("${orion.websocket.prefix}")
    private String prefix;

    @Resource
    private TerminalAccessInterceptor terminalAccessInterceptor;

    @Resource
    private TerminalTransferInterceptor terminalTransferInterceptor;

    @Resource
    private ExecLogTailInterceptor execLogTailInterceptor;

    @Resource
    private TerminalMessageDispatcher terminalMessageDispatcher;

    @Resource
    private TransferMessageDispatcher transferMessageDispatcher;

    @Resource
    private ExecLogTailHandler execLogTailHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 终端会话
        registry.addHandler(terminalMessageDispatcher, prefix + "/terminal/access/{accessToken}")
                .addInterceptors(terminalAccessInterceptor)
                .setAllowedOrigins("*");
        // 文件传输
        registry.addHandler(transferMessageDispatcher, prefix + "/terminal/transfer/{transferToken}")
                .addInterceptors(terminalTransferInterceptor)
                .setAllowedOrigins("*");
        // 执行日志
        registry.addHandler(execLogTailHandler, prefix + "/exec/log/{token}")
                .addInterceptors(execLogTailInterceptor)
                .setAllowedOrigins("*");
    }

}
