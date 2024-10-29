/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.enums.BooleanBit;
import org.dromara.visor.module.asset.define.operator.TerminalOperatorType;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.model.request.SftpChangeModeRequest;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import org.dromara.visor.module.asset.handler.host.terminal.session.ISftpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * sftp 修改文件权限
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpChangeModeHandler extends AbstractTerminalHandler<SftpChangeModeRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpChangeModeRequest payload) {
        long startTime = System.currentTimeMillis();
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        Integer mod = payload.getMod();
        log.info("SftpChangeModeHandler-handle start sessionId: {}, path: {}, mod: {}", sessionId, path, mod);
        Exception ex = null;
        // 修改权限
        try {
            session.chmod(path, mod);
            log.info("SftpChangeModeHandler-handle success sessionId: {}, path: {}, mod: {}", sessionId, path, mod);
        } catch (Exception e) {
            log.error("SftpChangeModeHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_CHMOD,
                SftpBaseResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        extra.put(OperatorLogs.MOD, mod);
        this.saveOperatorLog(payload, channel,
                extra, TerminalOperatorType.SFTP_CHMOD,
                startTime, ex);
    }

}
