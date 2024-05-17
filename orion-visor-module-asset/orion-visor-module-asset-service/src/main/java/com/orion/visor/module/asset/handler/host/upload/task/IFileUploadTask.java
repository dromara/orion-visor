package com.orion.visor.module.asset.handler.host.upload.task;

import com.orion.lang.able.SafeCloseable;
import com.orion.visor.module.asset.handler.host.upload.uploader.IFileUploader;

import java.util.List;

/**
 * 上传任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 12:28
 */
public interface IFileUploadTask extends Runnable, SafeCloseable {

    /**
     * 取消上传
     */
    void cancel();

    /**
     * 获取上传器
     *
     * @return uploader
     */
    List<IFileUploader> getUploaderList();

}
