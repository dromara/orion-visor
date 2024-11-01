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
package org.dromara.visor.module.asset.handler.host.exec.command.manager;

import org.dromara.visor.module.asset.handler.host.exec.command.handler.IExecTaskHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 执行管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/13 11:20
 */
@Component
public class ExecTaskManager {

    private final ConcurrentHashMap<Long, IExecTaskHandler> execTasks = new ConcurrentHashMap<>();

    /**
     * 添加任务
     *
     * @param id   id
     * @param task task
     */
    public void addTask(Long id, IExecTaskHandler task) {
        execTasks.put(id, task);
    }

    /**
     * 获取任务
     *
     * @param id id
     * @return task
     */
    public IExecTaskHandler getTask(Long id) {
        return execTasks.get(id);
    }

    /**
     * 移除任务
     *
     * @param id id
     */
    public void removeTask(Long id) {
        execTasks.remove(id);
    }

}
