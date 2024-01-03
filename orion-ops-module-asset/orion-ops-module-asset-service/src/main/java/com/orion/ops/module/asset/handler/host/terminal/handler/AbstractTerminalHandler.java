package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.alibaba.fastjson.JSONObject;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.handler.host.terminal.entity.Message;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputOperatorTypeEnum;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端消息处理器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 18:59
 */
public abstract class AbstractTerminalHandler<T> implements ITerminalHandler {

    /**
     * 类型
     */
    private final Class<T> convert;

    public AbstractTerminalHandler(Class<T> convert) {
        this.convert = convert;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(WebSocketSession session, Message<?> message) {
        Message<T> res = (Message<T>) message;
        res.setBody(((JSONObject) message.getBody()).toJavaObject(convert));
        this.handle(session, res);
    }

    /**
     * 处理消息
     *
     * @param session session
     * @param msg     msg
     */
    protected abstract void handle(WebSocketSession session, Message<T> msg);

    /**
     * 获取响应结构
     *
     * @param in   in
     * @param type type
     * @param body body
     * @param <E>  E
     * @return out
     */
    public <E> Message<E> out(Message<?> in, OutputOperatorTypeEnum type, E body) {
        return Message.<E>builder()
                .session(in.getSession())
                .type(type.getType())
                .body(body)
                .build();
    }

    /**
     * 发送消息
     *
     * @param session session
     * @param in      in
     * @param type    type
     * @param body    body
     */
    public void send(WebSocketSession session, Message<?> in, OutputOperatorTypeEnum type, Object body) {
        // 发送消息
        this.send(session, this.out(in, type, body));
    }

    /**
     * 发送消息
     *
     * @param session session
     * @param message message
     */
    protected void send(WebSocketSession session, Message<?> message) {
        WebSockets.sendJson(session, message);
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
