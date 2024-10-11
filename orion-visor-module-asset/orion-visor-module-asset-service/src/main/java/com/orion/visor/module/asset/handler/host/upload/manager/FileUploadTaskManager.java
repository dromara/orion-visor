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
package com.orion.visor.module.asset.handler.host.upload.manager;

import com.orion.visor.module.asset.handler.host.upload.task.IFileUploadTask;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件上传管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 17:26
 */
@Component
public class FileUploadTaskManager {

    private final ConcurrentHashMap<Long, IFileUploadTask> tasks = new ConcurrentHashMap<>();

    /**
     * 添加任务
     *
     * @param id   id
     * @param task task
     */
    public void addTask(Long id, IFileUploadTask task) {
        tasks.put(id, task);
    }

    /**
     * 移除任务
     *
     * @param id id
     */
    public void removeTask(Long id) {
        tasks.remove(id);
    }

    /**
     * 获取任务
     *
     * @param id id
     * @return task
     */
    public IFileUploadTask getTask(Long id) {
        return tasks.get(id);
    }

}
