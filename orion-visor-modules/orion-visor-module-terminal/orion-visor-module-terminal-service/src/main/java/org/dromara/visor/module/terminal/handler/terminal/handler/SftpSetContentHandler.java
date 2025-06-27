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

import cn.orionsec.kit.lang.id.UUIds;
import cn.orionsec.kit.lang.utils.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.terminal.define.cache.TerminalCacheKeyDefine;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.entity.dto.SftpSetContentCacheDTO;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.request.SftpBaseRequest;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ISftpSession;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * sftp 设置文件内容 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpSetContentHandler extends AbstractTerminalHandler<ISftpTerminalSender, SftpBaseRequest> {

    @Override
    public void handle(TerminalChannelProps props, ISftpTerminalSender sender, SftpBaseRequest payload) {
        long startTime = System.currentTimeMillis();
        String sessionId = props.getId();
        // 获取会话
        ISftpSession session = terminalManager.getSession(sessionId);
        String path = payload.getPath();
        log.info("SftpSetContentHandler-handle start sessionId: {}, path: {}", sessionId, path);
        String token = UUIds.random32();
        Exception ex = null;
        try {
            // 检查文件编辑权限
            session.checkEditPermission(path);
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
        // 返回结果
        sender.sendSetContentResult(ex == null, this.getErrorMessage(ex), token);
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        this.saveOperatorLog(props,
                extra, TerminalOperatorType.SFTP_SET_CONTENT,
                startTime, ex);
    }

}
