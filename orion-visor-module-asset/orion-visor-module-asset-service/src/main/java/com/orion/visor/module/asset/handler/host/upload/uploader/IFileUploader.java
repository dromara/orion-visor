package com.orion.visor.module.asset.handler.host.upload.uploader;

import com.orion.lang.able.SafeCloseable;
import com.orion.visor.module.asset.handler.host.upload.dto.FileUploadFileItemDTO;

import java.util.List;

/**
 * 主机文件上传器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 13:41
 */
public interface IFileUploader extends Runnable, SafeCloseable {

    /**
     * 取消上传
     */
    void cancel();

    /**
     * 获取传输文件
     *
     * @return files
     */
    List<FileUploadFileItemDTO> getFiles();

}
