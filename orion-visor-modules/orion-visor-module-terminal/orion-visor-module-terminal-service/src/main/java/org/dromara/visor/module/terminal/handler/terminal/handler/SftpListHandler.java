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

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.request.SftpListRequest;
import org.dromara.visor.module.terminal.handler.terminal.model.response.SftpFileVO;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ISftpSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * sftp 文件列表 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class SftpListHandler extends AbstractTerminalHandler<ISftpTerminalSender, SftpListRequest> {

    private static final String HOME_PATH = "~";

    @Override
    public void handle(TerminalChannelProps props, ISftpTerminalSender sender, SftpListRequest payload) {
        // 获取会话
        String sessionId = props.getId();
        ISftpSession session = terminalManager.getSession(sessionId);
        String path = payload.getPath();
        log.info("SftpListHandler-handle start sessionId: {}, path: {}", sessionId, path);
        Exception ex = null;
        List<SftpFileVO> list = Lists.empty();
        try {
            // 空目录则直接获取 home 目录
            if (HOME_PATH.equals(path)) {
                path = session.getHome();
            }
            // 文件列表
            list = session.list(path, BooleanBit.toBoolean(payload.getShowHiddenFile()));
            log.info("SftpListHandler-handle success sessionId: {}, path: {}", sessionId, path);
        } catch (Exception e) {
            log.error("SftpListHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回结果
        sender.sendFileList(path, ex == null, this.getErrorMessage(ex), list);
    }

}
