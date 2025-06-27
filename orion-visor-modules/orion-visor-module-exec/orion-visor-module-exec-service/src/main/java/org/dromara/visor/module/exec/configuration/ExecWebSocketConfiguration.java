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
package org.dromara.visor.module.exec.configuration;

import org.dromara.visor.module.exec.handler.exec.log.ExecLogTailHandler;
import org.dromara.visor.module.exec.interceptor.ExecLogTailInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 执行模块 websocket 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 11:39
 */
@Configuration
public class ExecWebSocketConfiguration implements WebSocketConfigurer {

    @Value("${orion.websocket.prefix}")
    private String prefix;

    @Resource
    private ExecLogTailInterceptor execLogTailInterceptor;

    @Resource
    private ExecLogTailHandler execLogTailHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 执行日志
        registry.addHandler(execLogTailHandler, prefix + "/exec/log/{token}")
                .addInterceptors(execLogTailInterceptor)
                .setAllowedOrigins("*");
    }

}
