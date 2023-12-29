package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.orion.ops.module.asset.handler.host.terminal.entity.MessageWrapper;
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
     * 类型转换器
     */
    private final TypeReference<MessageWrapper<T>> convert;

    public AbstractTerminalHandler(TypeReference<MessageWrapper<T>> convert) {
        this.convert = convert;
    }

    @Override
    public void process(WebSocketSession session, String payload) {
        this.onMessage(session, JSON.parseObject(payload, convert));
    }

    /**
     * 处理消息
     *
     * @param session session
     * @param msg     msg
     */
    protected abstract void onMessage(WebSocketSession session, MessageWrapper<T> msg);

}
