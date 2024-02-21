package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.able.SafeCloseable;

import java.io.IOException;

/**
 * 主机传输会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 23:06
 */
public interface ITransferHostSession extends SafeCloseable {

    /**
     * 初始化
     */
    void init();

    /**
     * 开始上传
     *
     * @param path path
     * @throws IOException IOException
     */
    void startUpload(String path) throws IOException;

    /**
     * 写入内容
     *
     * @param bytes bytes
     * @throws IOException IOException
     */
    void putContent(byte[] bytes) throws IOException;

    /**
     * 写入完成
     */
    void putFinish();

}
