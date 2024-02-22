package com.orion.ops.module.asset.handler.host.transfer.session;

/**
 * 下载会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:25
 */
public interface IDownloadSession {

    /**
     * 开始下载
     *
     * @param path path
     */
    void startDownload(String path);

    /**
     * 停止下载
     */
    void abortDownload();

}
