package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端消息处理器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 18:59
 */
public abstract class AbstractTerminalHandler<T extends TerminalBasePayload> implements ITerminalHandler<T> {

    /**
     * 发送消息
     *
     * @param session session
     * @param type    type
     * @param body    body
     * @param <E>     E
     */
    public <E extends TerminalBasePayload> void send(WebSocketSession session, OutputTypeEnum type, E body) {
        body.setType(type.getType());
        // 发送消息
        this.send(session, type.format(body));
    }

    /**
     * 发送消息
     *
     * @param session session
     * @param message message
     */
    protected void send(WebSocketSession session, String message) {
        WebSockets.sendText(session, message);
    }

    /**
     * 获取属性
     *
     * @param session session
     * @param attr    attr
     * @param <E>     T
     * @return T
     */
    @SuppressWarnings("unchecked")
    protected <E> E getAttr(WebSocketSession session, String attr) {
        return (E) session.getAttributes().get(attr);
    }

}
