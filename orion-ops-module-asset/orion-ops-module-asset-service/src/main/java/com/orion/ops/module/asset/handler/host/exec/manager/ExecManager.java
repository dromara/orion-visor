package com.orion.ops.module.asset.handler.host.exec.manager;

import com.orion.ops.module.asset.handler.host.exec.handler.IExecTaskHandler;
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
public class ExecManager {

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
