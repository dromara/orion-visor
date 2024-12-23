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
package org.dromara.visor.module.asset.handler.host.exec.log.tracker;

import cn.orionsec.kit.ext.tail.Tracker;
import cn.orionsec.kit.ext.tail.delay.DelayTrackerListener;
import cn.orionsec.kit.ext.tail.mode.FileNotFoundMode;
import cn.orionsec.kit.ext.tail.mode.FileOffsetMode;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.asset.define.config.AppTrackerConfig;
import org.dromara.visor.module.asset.entity.dto.ExecHostLogTailDTO;
import org.dromara.visor.module.asset.handler.host.exec.log.constant.LogConst;
import org.springframework.web.socket.WebSocketSession;

/**
 * log tracker 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 23:36
 */
@Slf4j
public class ExecLogTracker implements IExecLogTracker {

    private static final AppTrackerConfig trackerConfig = SpringHolder.getBean(AppTrackerConfig.class);

    private final WebSocketSession session;

    private final ExecHostLogTailDTO config;

    @Getter
    private final String trackerId;

    @Getter
    private final String absolutePath;

    private DelayTrackerListener tracker;

    private volatile boolean close;

    public ExecLogTracker(String trackerId,
                          String absolutePath,
                          WebSocketSession session,
                          ExecHostLogTailDTO config) {
        this.trackerId = trackerId;
        this.absolutePath = absolutePath;
        this.session = session;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            this.tracker = new DelayTrackerListener(absolutePath, this);
            tracker.charset(config.getCharset());
            tracker.delayMillis(trackerConfig.getDelay());
            tracker.offset(FileOffsetMode.LINE, trackerConfig.getOffset());
            tracker.notFoundMode(FileNotFoundMode.WAIT_COUNT, trackerConfig.getWaitTimes());
            // 开始监听文件
            tracker.run();
        } catch (Exception e) {
            log.error("exec log tracker error path: {}", absolutePath, e);
        } finally {
            // 释放资源
            this.close();
        }
    }

    @Override
    public void setLastModify() {
        tracker.setFileLastModifyTime();
    }

    @Override
    public String getPath() {
        return config.getPath();
    }

    @Override
    public void read(byte[] bytes, int len, Tracker tracker) {
        // 发送消息
        String message = config.getId() + LogConst.SEPARATOR + new String(bytes, 0, len);
        try {
            WebSockets.sendText(session, message);
        } catch (Exception e) {
            log.error("ExecLogTracker.send error", e);
        }
    }

    @Override
    public void close() {
        log.info("ExecLogTracker.close path: {}, closed: {}", absolutePath, close);
        if (close) {
            return;
        }
        this.close = true;
        if (tracker != null) {
            tracker.stop();
        }
    }

}
