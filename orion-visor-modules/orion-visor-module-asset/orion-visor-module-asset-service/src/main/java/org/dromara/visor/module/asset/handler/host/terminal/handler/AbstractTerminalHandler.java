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
package org.dromara.visor.module.asset.handler.host.terminal.handler;

import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.manager.TerminalManager;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import org.dromara.visor.module.asset.handler.host.terminal.session.ITerminalSession;
import org.dromara.visor.module.asset.handler.host.terminal.utils.TerminalUtils;
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
        // 获取错误信息
        return ErrorMessage.getErrorMessage(ex, ErrorMessage.OPERATE_ERROR);
    }

}
