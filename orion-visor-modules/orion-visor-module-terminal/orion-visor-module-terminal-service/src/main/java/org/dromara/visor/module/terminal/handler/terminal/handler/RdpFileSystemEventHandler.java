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
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.request.RdpFileSystemEventRequest;
import org.dromara.visor.module.terminal.handler.terminal.model.transport.RdpFileSystemEvent;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * rdp 文件系统事件 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class RdpFileSystemEventHandler extends AbstractTerminalHandler<IGuacdTerminalSender, RdpFileSystemEventRequest> {

    @Override
    public void handle(TerminalChannelProps props, IGuacdTerminalSender sender, RdpFileSystemEventRequest payload) {
        long startTime = System.currentTimeMillis();
        String sessionId = props.getId();
        // 获取会话
        RdpFileSystemEvent fsEvent = JSON.parseObject(payload.getEvent(), RdpFileSystemEvent.class);
        String event = fsEvent.getEvent();
        String path = fsEvent.getPath();
        log.info("RdpFileSystemEventHandler-handle start sessionId: {}, event: {}, path: {}", sessionId, event, path);
        String operatorType;
        if (TerminalOperatorType.RDP_UPLOAD.equals(event)) {
            // 上传文件
            operatorType = TerminalOperatorType.RDP_UPLOAD;
        } else if (TerminalOperatorType.RDP_DOWNLOAD.equals(event)) {
            // 下载文件
            operatorType = TerminalOperatorType.RDP_DOWNLOAD;
        } else {
            return;
        }
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        this.saveOperatorLog(props, extra, operatorType, startTime, null);
    }

}
