package com.orion.ops.module.infra.handler.upload.handler;

import com.orion.lang.able.SafeCloseable;

/**
 * 文件上传处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 15:49
 */
public interface IFileUploadHandler extends SafeCloseable {

    /**
     * 开始上传
     *
     * @param fileId fileId
     */
    void start(String fileId);

    /**
     * 写入内容
     *
     * @param content content
     */
    void write(byte[] content);

    /**
     * 上传结束
     */
    void finish();

}
