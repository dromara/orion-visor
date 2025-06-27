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

import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.request.SftpChangeOwnerRequest;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ISftpSession;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * sftp 修改文件归属 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpChangeOwnerHandler extends AbstractTerminalHandler<ISftpTerminalSender, SftpChangeOwnerRequest> {

    @Override
    public void handle(TerminalChannelProps props, ISftpTerminalSender sender, SftpChangeOwnerRequest payload) {
        long startTime = System.currentTimeMillis();
        // 获取会话
        String sessionId = props.getId();
        ISftpSession session = terminalManager.getSession(sessionId);
        String path = payload.getPath();
        Integer uid = payload.getUid();
        log.info("SftpChangeOwnerHandler-handle start sessionId: {}, path: {}, uid: {}", sessionId, path, uid);
        Exception ex = null;
        // 修改归属
        try {
            session.changeOwner(path, uid);
            log.info("SftpChangeOwnerHandler-handle success sessionId: {}, path: {}, uid: {}", sessionId, path, uid);
        } catch (Exception e) {
            log.error("SftpChangeOwnerHandler-handle error sessionId: {},", sessionId, e);
            ex = e;
        }
        // 返回结果
        sender.sendChangeOwnerResult(ex == null, this.getErrorMessage(ex));
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        extra.put(OperatorLogs.ID, uid);
        this.saveOperatorLog(props,
                extra, TerminalOperatorType.SFTP_CHOWN,
                startTime, ex);
    }

}
