package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.alibaba.fastjson.JSONObject;
import com.orion.ops.module.asset.handler.host.terminal.entity.Message;
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
     * 获取属性
     *
     * @param session session
     * @param attr    attr
     * @param <T>     T
     * @return T
     */
    @SuppressWarnings("unchecked")
    protected <T> T getAttr(WebSocketSession session, String attr) {
        return (T) session.getAttributes().get(attr);
    }

}
