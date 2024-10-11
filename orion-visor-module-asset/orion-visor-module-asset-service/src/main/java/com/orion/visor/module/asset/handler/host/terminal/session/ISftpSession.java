/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.asset.handler.host.terminal.session;

import com.orion.visor.module.asset.handler.host.terminal.model.response.SftpFileVO;

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
    List<SftpFileVO> list(String path, boolean showHiddenFile);

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
    void remove(String[] paths);

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
     * 展开文件夹内的所有文件
     *
     * @param paths paths
     * @return files
     */
    List<SftpFileVO> flatDirectory(String[] paths);

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

    /**
     * 检测文件是否可编辑
     *
     * @param path path
     */
    void checkCanEdit(String path);

}
