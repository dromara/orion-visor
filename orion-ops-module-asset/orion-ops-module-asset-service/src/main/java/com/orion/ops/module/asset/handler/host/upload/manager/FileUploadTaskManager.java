package com.orion.ops.module.asset.handler.host.upload.manager;

import com.orion.ops.module.asset.handler.host.upload.task.IFileUploadTask;
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
