package com.orion.ops.module.asset.handler.host.transfer.session;

/**
 * 上传会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:03
 */
public interface IUploadSession extends ITransferHostSession {

    /**
     * 开始上传
     *
     * @param path path
     */
    void startUpload(String path);

    /**
     * 写入内容
     *
     * @param bytes bytes
     */
    void putContent(byte[] bytes);

    /**
     * 上传完成
     */
    void uploadFinish();

    /**
     * 上传失败
     */
    void uploadError();

}
