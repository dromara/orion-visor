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
package org.dromara.visor.module.terminal.handler.terminal.manager;

import cn.orionsec.kit.lang.utils.io.Streams;
import lombok.Getter;
import org.dromara.visor.module.terminal.handler.terminal.session.ITerminalSession;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 终端管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 11:35
 */
@Getter
@Component
public class TerminalManager {

    /**
     * 会话映射
     */
    private final ConcurrentHashMap<String, ITerminalSession> channelSessionMap = new ConcurrentHashMap<>();

    /**
     * 添加会话
     *
     * @param sessionId sessionId
     * @param session   session
     */
    public void addSession(String sessionId, ITerminalSession session) {
        channelSessionMap.put(sessionId, session);
    }

    /**
     * 通过 channel 关闭会话
     *
     * @param sessionId sessionId
     */
    public void closeSession(String sessionId) {
        // 获取并移除
        ITerminalSession session = channelSessionMap.remove(sessionId);
        // 关闭会话
        this.doCloseSession(session);
    }

    /**
     * 获取会话
     *
     * @param sessionId sessionId
     * @param <T>       T
     * @return session
     */
    @SuppressWarnings("unchecked")
    public <T extends ITerminalSession> T getSession(String sessionId) {
        return (T) channelSessionMap.get(sessionId);
    }

    /**
     * 执行关闭会话
     *
     * @param session session
     */
    private void doCloseSession(ITerminalSession session) {
        if (session == null) {
            return;
        }
        // 关闭会话
        Streams.close(session);
    }

}
