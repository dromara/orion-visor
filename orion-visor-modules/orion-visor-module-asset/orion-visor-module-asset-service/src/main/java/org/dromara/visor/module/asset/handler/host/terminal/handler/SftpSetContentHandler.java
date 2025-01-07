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

import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.enums.BooleanBit;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.asset.define.cache.TerminalCacheKeyDefine;
import org.dromara.visor.module.asset.define.operator.TerminalOperatorType;
import org.dromara.visor.module.asset.entity.dto.SftpSetContentCacheDTO;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.model.request.SftpBaseRequest;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.SftpSetContentResponse;
import org.dromara.visor.module.asset.handler.host.terminal.session.ISftpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * sftp 设置文件内容
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpSetContentHandler extends AbstractTerminalHandler<SftpBaseRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpBaseRequest payload) {
        long startTime = System.currentTimeMillis();
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        log.info("SftpSetContentHandler-handle start sessionId: {}, path: {}", sessionId, path);
        String token = UUIds.random32();
        Exception ex = null;
        try {
            // 检查文件是否可编辑
            session.checkCanEdit(path);
            // 设置缓存
            String key = TerminalCacheKeyDefine.TERMINAL_SFTP_SET_CONTENT.format(token);
            SftpSetContentCacheDTO cache = SftpSetContentCacheDTO.builder()
                    .hostId(session.getConfig().getHostId())
                    .path(path)
                    .build();
            RedisStrings.setJson(key, TerminalCacheKeyDefine.TERMINAL_SFTP_SET_CONTENT, cache);
            log.info("SftpSetContentHandler-handle success sessionId: {}, path: {}", sessionId, path);
        } catch (Exception e) {
            log.error("SftpSetContentHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_SET_CONTENT,
                SftpSetContentResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .token(token)
                        .msg(this.getErrorMessage(ex))
                        .build());
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        this.saveOperatorLog(payload, channel,
                extra, TerminalOperatorType.SFTP_SET_CONTENT,
                startTime, ex);
    }

}
