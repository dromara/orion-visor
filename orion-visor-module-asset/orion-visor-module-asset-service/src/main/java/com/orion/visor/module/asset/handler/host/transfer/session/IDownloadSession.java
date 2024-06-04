package com.orion.visor.module.asset.handler.host.transfer.session;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * 下载会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:25
 */
public interface IDownloadSession extends StreamingResponseBody {

    /**
     * 初始化下载
     *
     * @param path  path
     * @param token token
     */
    void downloadInit(String path, String token);

    /**
     * 停止下载
     */
    void abortDownload();

    /**
     * 获取下载文件路径
     *
     * @return path
     */
    String getPath();

}
