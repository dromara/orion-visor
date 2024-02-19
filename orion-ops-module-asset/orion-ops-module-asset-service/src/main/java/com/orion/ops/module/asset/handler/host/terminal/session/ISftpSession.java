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

    /**
     * 创建文件夹
     *
     * @param path path
     */
    void mkdir(String path);

    /**
     * 创建文件
     *
     * @param path path
     */
    void touch(String path);

    /**
     * 移动文件
     *
     * @param source source
     * @param target target
     */
    void move(String source, String target);

    /**
     * 删除文件
     *
     * @param paths paths
     */
    void remove(List<String> paths);

    /**
     * 截断文件
     *
     * @param path path
     */
    void truncate(String path);

    /**
     * 修改权限
     *
     * @param path path
     * @param mod  mod
     */
    void chmod(String path, int mod);

    /**
     * 获取内容
     *
     * @param path path
     * @return content
     */
    String getContent(String path);

    /**
     * 设置内容
     *
     * @param path    path
     * @param content content
     */
    void setContent(String path, String content);

}
