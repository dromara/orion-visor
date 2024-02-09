package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpFileResponse;

import java.util.List;

/**
 * sftp 会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/4 16:48
 */
public interface ISftpSession extends ITerminalSession {

    /**
     * 建立连接
     */
    void connect();

    /**
     * 获取 home 路径
     *
     * @return homePath
     */
    String getHome();

    /**
     * 文件列表
     *
     * @param path           path
     * @param showHiddenFile 是否显示隐藏文件
     * @return list
     */
    List<SftpFileResponse> list(String path, boolean showHiddenFile);

}
