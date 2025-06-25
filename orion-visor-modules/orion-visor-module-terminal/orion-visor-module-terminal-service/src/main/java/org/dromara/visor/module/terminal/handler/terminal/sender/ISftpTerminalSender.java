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
package org.dromara.visor.module.terminal.handler.terminal.sender;

import org.dromara.visor.module.terminal.handler.terminal.model.response.SftpFileVO;

import java.util.List;

/**
 * SFTP 消息发送器定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/31 14:33
 */
public interface ISftpTerminalSender extends ITerminalSender {

    /**
     * 发送 SFTP 设置文件列表结果
     *
     * @param path    path
     * @param result  result
     * @param message message
     * @param list    list
     */
    default void sendFileList(String path, boolean result, String message, List<SftpFileVO> list) {
    }

    /**
     * 发送 SFTP 创建文件夹结果
     *
     * @param result  result
     * @param message message
     */
    default void sendMakeDirectoryResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 创建文件结果
     *
     * @param result  result
     * @param message message
     */
    default void sendTouchResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 移动文件结果
     *
     * @param result  result
     * @param message message
     */
    default void sendMoveResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 删除文件结果
     *
     * @param result  result
     * @param message message
     */
    default void sendRemoveResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 截断文件结果
     *
     * @param result  result
     * @param message message
     */
    default void sendTruncateResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 修改文件权限结果
     *
     * @param result  result
     * @param message message
     */
    default void sendChangeModeResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 修改文件归属结果
     *
     * @param result  result
     * @param message message
     */
    default void sendChangeOwnerResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 修改文件分组结果
     *
     * @param result  result
     * @param message message
     */
    default void sendChangeGroupResult(boolean result, String message) {
    }

    /**
     * 发送 SFTP 下载文件夹展开文件结果
     *
     * @param currentPath currentPath
     * @param result      result
     * @param message     message
     * @param list        list
     */
    default void sendDownloadFlatDirectory(String currentPath, boolean result, String message, List<SftpFileVO> list) {
    }

    /**
     * 发送 SFTP 获取文件内容结果
     *
     * @param result  result
     * @param message message
     * @param token   token
     */
    default void sendGetContentResult(boolean result, String message, String token) {
    }

    /**
     * 发送 SFTP 修改文件内容结果
     *
     * @param result  result
     * @param message message
     * @param token   token
     */
    default void sendSetContentResult(boolean result, String message, String token) {
    }

}
