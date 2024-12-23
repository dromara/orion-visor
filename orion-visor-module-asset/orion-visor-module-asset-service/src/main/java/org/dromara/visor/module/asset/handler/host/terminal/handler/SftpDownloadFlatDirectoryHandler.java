/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.enums.BooleanBit;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.model.request.SftpDownloadFlatDirectoryRequest;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.SftpDownloadFlatDirectoryResponse;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.SftpFileVO;
import org.dromara.visor.module.asset.handler.host.terminal.session.ISftpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;
import java.util.List;

/**
 * sftp 下载文件夹展开文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpDownloadFlatDirectoryHandler extends AbstractTerminalHandler<SftpDownloadFlatDirectoryRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpDownloadFlatDirectoryRequest payload) {
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String[] paths = payload.getPath().split("\\|");
        log.info("SftpDownloadFlatDirectoryHandler-handle start sessionId: {}, paths: {}", sessionId, Arrays.toString(paths));
        Exception ex = null;
        List<SftpFileVO> files = Lists.empty();
        // 展开文件夹内的全部文件
        try {
            files = session.flatDirectory(paths);
            log.info("SftpDownloadFlatDirectoryHandler-handle success sessionId: {}, paths: {}", sessionId, Arrays.toString(paths));
        } catch (Exception e) {
            log.error("SftpDownloadFlatDirectoryHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_DOWNLOAD_FLAT_DIRECTORY,
                SftpDownloadFlatDirectoryResponse.builder()
                        .sessionId(sessionId)
                        .currentPath(payload.getCurrentPath())
                        .body(JSON.toJSONString(files))
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
    }

}
