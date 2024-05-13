package com.orion.ops.module.asset.handler.host.upload;

import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.handler.host.upload.task.FileUploadTask;

/**
 * 批量上传执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 17:23
 */
public class FileUploadTasks {

    private FileUploadTasks() {
    }

    /**
     * 上传
     *
     * @param taskId taskId
     */
    public static void start(Long taskId) {
        AssetThreadPools.UPLOAD_TASK.execute(new FileUploadTask(taskId));
    }

}
