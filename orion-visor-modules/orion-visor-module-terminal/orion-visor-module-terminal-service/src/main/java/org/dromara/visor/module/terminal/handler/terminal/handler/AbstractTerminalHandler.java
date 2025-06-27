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
package org.dromara.visor.module.terminal.handler.terminal.handler;

import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.terminal.handler.terminal.constant.SessionCloseCode;
import org.dromara.visor.module.terminal.handler.terminal.manager.TerminalManager;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.ITerminalSessionConfig;
import org.dromara.visor.module.terminal.handler.terminal.record.TerminalAsyncSaver;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ITerminalSession;
import org.dromara.visor.module.terminal.handler.terminal.utils.TerminalUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 终端消息处理器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 18:59
 */
public abstract class AbstractTerminalHandler<S extends ITerminalSender, T extends TerminalBasePayload>
        implements ITerminalHandler<S, T> {

    @Resource
    protected TerminalManager terminalManager;

    /**
     * 保存操作日志
     *
     * @param props     props
     * @param extra     extra
     * @param type      type
     * @param startTime startTime
     * @param ex        ex
     * @return model
     */
    protected OperatorLogModel saveOperatorLog(TerminalChannelProps props,
                                               Map<String, Object> extra,
                                               String type,
                                               long startTime,
                                               Exception ex) {
        String sessionId = props.getId();
        // 获取会话并且设置参数
        ITerminalSession session = terminalManager.getSession(sessionId);
        if (session != null) {
            ITerminalSessionConfig config = session.getConfig();
            extra.put(OperatorLogs.HOST_ID, config.getHostId());
            extra.put(OperatorLogs.HOST_NAME, config.getHostName());
            extra.put(OperatorLogs.ADDRESS, config.getHostAddress());
        }
        extra.put(OperatorLogs.SESSION_ID, sessionId);
        // 获取日志
        OperatorLogModel model = TerminalUtils.getOperatorLogModel(props, extra, type, startTime, ex);
        // 保存操作日志
        TerminalAsyncSaver.saveOperatorLog(model);
        return model;
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

    /**
     * 发送 closed 消息以及关闭
     *
     * @param sender  sender
     * @param message message
     */
    protected void sendClosedAndClose(S sender, String message) {
        this.sendClosedAndClose(sender, SessionCloseCode.NORMAL, message);
    }

    /**
     * 发送 closed 消息以及关闭
     *
     * @param sender  sender
     * @param code    code
     * @param message message
     */
    protected void sendClosedAndClose(S sender, int code, String message) {
        // 发送消息
        sender.sendClosed(code, message);
        // 关闭 sender
        sender.close();
    }

}
