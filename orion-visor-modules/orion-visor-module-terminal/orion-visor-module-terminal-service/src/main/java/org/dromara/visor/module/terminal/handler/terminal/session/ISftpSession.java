/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.terminal.handler.terminal.session;

import org.dromara.visor.module.terminal.handler.terminal.model.response.SftpFileVO;

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
    void changeMode(String path, int mod);

    /**
     * 修改归属
     *
     * @param path path
     * @param uid  uid
     */
    void changeOwner(String path, int uid);

    /**
     * 修改分组
     *
     * @param path path
     * @param gid  gid
     */
    void changeGroup(String path, int gid);

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
     * 检测是否有文件编辑的权限
     *
     * @param path path
     */
    void checkEditPermission(String path);

}
