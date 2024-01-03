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
    private final MultiConcurrentHashMap<String, String, ITerminalSession> sessions = MultiConcurrentHashMap.create();

    /**
     * 添加会话
     *
     * @param terminalSession terminalSession
     */
    public void addSession(TerminalSession terminalSession) {
        sessions.put(terminalSession.getSession().getId(), terminalSession.getToken(), terminalSession);
    }

    /**
     * 获取会话
     *
     * @param id    id
     * @param token token
     * @return session
     */
    public ITerminalSession getSession(String id, String token) {
        return sessions.get(id, token);
    }

    /**
     * 关闭会话
     *
     * @param id    id
     * @param token token
     */
    public void closeSession(String id, String token) {
        // 获取并移除
        ITerminalSession session = sessions.removeElement(id, token);
        if (session != null) {
            Streams.close(session);
        }
    }

    /**
     * 关闭全部会话
     *
     * @param id id
     */
    public void closeAll(String id) {
        // 获取并移除
        ConcurrentHashMap<String, ITerminalSession> session = sessions.remove(id);
        if (Maps.isEmpty(session)) {
            session.values().forEach(Streams::close);
        }
    }

}
