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
package org.dromara.visor.module.asset.handler.host.terminal.manager;

import cn.orionsec.kit.lang.define.collect.MultiConcurrentHashMap;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.dromara.visor.module.asset.handler.host.terminal.session.ITerminalSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 终端管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 11:35
 */
@Component
public class TerminalManager {

    /**
     * 会话存储器
     */
    private final MultiConcurrentHashMap<String, String, ITerminalSession> channelSessions = MultiConcurrentHashMap.create();

    /**
     * 添加会话
     *
     * @param session session
     */
    public void addSession(ITerminalSession session) {
        channelSessions.put(session.getChannelId(), session.getSessionId(), session);
    }

    /**
     * 通过 channel 关闭会话
     *
     * @param channelId channelId
     */
    public void closeSession(String channelId) {
        // 获取并移除
        ConcurrentHashMap<String, ITerminalSession> session = channelSessions.remove(channelId);
        if (!Maps.isEmpty(session)) {
            session.values().forEach(Streams::close);
        }
    }

    /**
     * 通过 channel + sessionId 关闭会话
     *
     * @param channelId channelId
     * @param sessionId sessionId
     */
    public void closeSession(String channelId, String sessionId) {
        // 获取并移除
        ITerminalSession session = channelSessions.removeElement(channelId, sessionId);
        if (session != null) {
            Streams.close(session);
        }
    }

    /**
     * 获取会话
     *
     * @param channelId channelId
     * @param sessionId sessionId
     * @param <T>       T
     * @return session
     */
    @SuppressWarnings("unchecked")
    public <T extends ITerminalSession> T getSession(String channelId, String sessionId) {
        return (T) channelSessions.get(channelId, sessionId);
    }

    /**
     * 获取会话
     *
     * @param channelId channelId
     * @return session
     */
    public Map<String, ITerminalSession> getSession(String channelId) {
        return channelSessions.get(channelId);
    }

    /**
     * 获取全部会话
     *
     * @return session
     */
    public MultiConcurrentHashMap<String, String, ITerminalSession> getChannelSessions() {
        return channelSessions;
    }

}
