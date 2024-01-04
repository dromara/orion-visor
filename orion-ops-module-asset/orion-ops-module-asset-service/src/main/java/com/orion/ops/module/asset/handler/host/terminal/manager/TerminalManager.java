package com.orion.ops.module.asset.handler.host.terminal.manager;

import com.orion.lang.define.collect.MultiConcurrentHashMap;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Streams;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.ops.module.asset.handler.host.terminal.session.TerminalSession;
import org.springframework.stereotype.Component;

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
    public void addSession(TerminalSession session) {
        channelSessions.put(session.getChannel().getId(), session.getSessionId(), session);
    }

    /**
     * 获取会话
     *
     * @param channelId channelId
     * @param sessionId sessionId
     * @return session
     */
    public ITerminalSession getSession(String channelId, String sessionId) {
        return channelSessions.get(channelId, sessionId);
    }

    /**
     * 关闭会话
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
     * 关闭全部会话
     *
     * @param channelId channelId
     */
    public void closeAll(String channelId) {
        // 获取并移除
        ConcurrentHashMap<String, ITerminalSession> session = channelSessions.remove(channelId);
        if (Maps.isEmpty(session)) {
            session.values().forEach(Streams::close);
        }
    }

}
