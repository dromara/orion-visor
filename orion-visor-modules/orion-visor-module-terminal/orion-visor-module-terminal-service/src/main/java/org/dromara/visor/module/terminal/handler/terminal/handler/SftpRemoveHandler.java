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
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.request.SftpBaseRequest;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ISftpSession;
import org.dromara.visor.module.terminal.utils.SftpFileUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * sftp 删除文件 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpRemoveHandler extends AbstractTerminalHandler<ISftpTerminalSender, SftpBaseRequest> {

    @Override
    public void handle(TerminalChannelProps props, ISftpTerminalSender sender, SftpBaseRequest payload) {
        long startTime = System.currentTimeMillis();
        String[] paths = SftpFileUtils.fromMultiPaths(payload.getPath());
        String path = String.join(Const.LF, paths);
        String sessionId = props.getId();
        // 获取会话
        ISftpSession session = terminalManager.getSession(sessionId);
        log.info("SftpRemoveHandler-handle start sessionId: {}, path: {}", sessionId, Arrays.toString(paths));
        Exception ex = null;
        // 删除
        try {
            session.remove(paths);
            log.info("SftpRemoveHandler-handle success sessionId: {}, path: {}", sessionId, Arrays.toString(paths));
        } catch (Exception e) {
            log.error("SftpRemoveHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回结果
        sender.sendRemoveResult(ex == null, this.getErrorMessage(ex));
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        extra.put(OperatorLogs.COUNT, paths.length);
        this.saveOperatorLog(props,
                extra, TerminalOperatorType.SFTP_REMOVE,
                startTime, ex);
    }

}
