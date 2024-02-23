package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.ops.module.asset.handler.host.terminal.utils.TerminalUtils;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Map;

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

    @Resource
    private OperatorLogFrameworkService operatorLogFrameworkService;

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
     * 保存操作日志
     *
     * @param payload   payload
     * @param channel   channel
     * @param extra     extra
     * @param type      type
     * @param startTime startTime
     * @param ex        ex
     */
    protected void saveOperatorLog(T payload,
                                   WebSocketSession channel,
                                   Map<String, Object> extra,
                                   String type,
                                   long startTime,
                                   Exception ex) {
        String channelId = channel.getId();
        String sessionId = payload.getSessionId();
        // 获取会话并且设置参数
        ITerminalSession session = terminalManager.getSession(channelId, sessionId);
        if (session != null) {
            TerminalConfig config = session.getConfig();
            extra.put(OperatorLogs.HOST_ID, config.getHostId());
            extra.put(OperatorLogs.HOST_NAME, config.getHostName());
            extra.put(OperatorLogs.ADDRESS, config.getAddress());
        }
        extra.put(OperatorLogs.CHANNEL_ID, channelId);
        extra.put(OperatorLogs.SESSION_ID, sessionId);
        // 获取日志
        OperatorLogModel model = TerminalUtils.getOperatorLogModel(channel, extra, type, startTime, ex);
        // 保存
        operatorLogFrameworkService.insert(model);
    }

    /**
     * 获取错误信息
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
