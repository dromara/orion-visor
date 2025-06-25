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
package org.dromara.visor.module.terminal.handler.terminal.session;

import org.dromara.visor.module.terminal.handler.terminal.model.config.ITerminalSessionConfig;
import cn.orionsec.kit.lang.able.SafeCloseable;

/**
 * 终端会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
public interface ITerminalSession extends SafeCloseable {

    /**
     * 建立连接
     */
    void connect();

    /**
     * 活跃会话
     */
    void keepAlive();

    /**
     * 强制下线
     */
    void forceOffline();

    /**
     * 设置强制下线
     */
    void setForcedOffline();

    /**
     * 设置关闭状态
     *
     * @param code    code
     * @param message message
     */
    void setCloseStatus(Integer code, String message);

    /**
     * 是否已关闭
     *
     * @return closed
     */
    boolean isClosed();

    /**
     * 是否已连接
     *
     * @return connected
     */
    boolean isConnected();

    /**
     * 是否已强制下线
     *
     * @return forcedOffline
     */
    boolean isForcedOffline();

    /**
     * 获取 logId
     *
     * @return logId
     */
    Long getLogId();

    /**
     * 获取配置
     *
     * @return config
     */
    <T extends ITerminalSessionConfig> T getConfig();

}
