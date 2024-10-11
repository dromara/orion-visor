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
package com.orion.visor.module.asset.handler.host.terminal.session;

import com.orion.lang.able.SafeCloseable;
import com.orion.visor.module.asset.handler.host.terminal.model.TerminalConfig;

/**
 * 终端会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
public interface ITerminalSession extends SafeCloseable {

    /**
     * 获取 sessionId
     *
     * @return sessionId
     */
    String getSessionId();

    /**
     * 获取 channelId
     *
     * @return channelId
     */
    String getChannelId();

    /**
     * 获取配置
     *
     * @return config
     */
    TerminalConfig getConfig();

    /**
     * 活跃会话
     */
    void keepAlive();

    /**
     * 强制下线
     */
    void forceOffline();

    /**
     * 是否已关闭
     *
     * @return closed
     */
    boolean isClosed();

}
