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
package org.dromara.visor.module.exec.handler.exec.log.manager;

import cn.orionsec.kit.lang.utils.Threads;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.exec.handler.exec.log.tracker.IExecLogTracker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 执行日志管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 23:36
 */
@Slf4j
@Component
public class ExecLogManager {

    private final ConcurrentHashMap<String, List<IExecLogTracker>> execTrackers = new ConcurrentHashMap<>();

    /**
     * 添加执行日志追踪器
     *
     * @param id      id
     * @param tracker tracker
     */
    public void addTracker(String id, IExecLogTracker tracker) {
        execTrackers.computeIfAbsent(id, k -> new ArrayList<>()).add(tracker);
    }

    /**
     * 移除日志追踪器
     *
     * @param id id
     */
    public void removeTrackers(String id) {
        // 移除并且关闭
        List<IExecLogTracker> trackers = execTrackers.remove(id);
        if (trackers != null) {
            trackers.forEach(IExecLogTracker::close);
        }
    }

    /**
     * 异步关闭进行中的追踪器
     *
     * @param execHostId execHostId
     */
    public void asyncCloseTailFile(Long execHostId) {
        if (execHostId == null) {
            return;
        }
        // 获取当前路径的全部追踪器
        List<IExecLogTracker> trackers = execTrackers.values()
                .stream()
                .flatMap(Collection::stream)
                .filter(s -> s.getExecHostId().equals(execHostId))
                .collect(Collectors.toList());
        if (trackers.isEmpty()) {
            return;
        }
        // 异步设置更新并且关闭
        Threads.start(() -> {
            try {
                Threads.sleep(Const.MS_S_1);
                trackers.forEach(IExecLogTracker::setLastModify);
                Threads.sleep(Const.MS_S_5);
                trackers.forEach(IExecLogTracker::close);
            } catch (Exception e) {
                log.error("ExecLogManager.asyncCloseTailFile error execHostId: {}", execHostId, e);
            }
        });
    }

}
