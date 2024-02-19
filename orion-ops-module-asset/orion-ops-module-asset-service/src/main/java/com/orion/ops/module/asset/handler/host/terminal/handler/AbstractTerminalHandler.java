package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * 终端消息处理器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 18:59
 */
public abstract class AbstractTerminalHandler<T extends TerminalBasePayload> implements ITerminalHandler<T> {

    @Resource
    protected TerminalManager terminalManager;

    /**
     * 发送消息
     *
     * @param channel channel
     * @param type    type
     * @param body    body
     * @param <E>     E
     */
    public <E extends TerminalBasePayload> void send(WebSocketSession channel, OutputTypeEnum type, E body) {
        body.setType(type.getType());
        // 发送消息
        this.send(channel, type.format(body));
    }

    /**
     * 发送消息
     *
     * @param channel channel
     * @param message message
     */
    protected void send(WebSocketSession channel, String message) {
        WebSockets.sendText(channel, message);
    }

    /**
     * 获取属性
     *
     * @param channel channel
     * @param attr    attr
     * @param <E>     T
     * @return T
     */
    @SuppressWarnings("unchecked")
    protected <E> E getAttr(WebSocketSession channel, String attr) {
        return (E) channel.getAttributes().get(attr);
    }

    /**
     * 获取 sftp 错误信息
     *
     * @param ex ex
     * @return msg
     */
    protected String getErrorMessage(Exception ex) {
        if (ex == null) {
            return null;
        }
        if (ex instanceof InvalidArgumentException) {
            return ex.getMessage();
        }
        return ErrorMessage.OPERATE_ERROR;
    }

}
