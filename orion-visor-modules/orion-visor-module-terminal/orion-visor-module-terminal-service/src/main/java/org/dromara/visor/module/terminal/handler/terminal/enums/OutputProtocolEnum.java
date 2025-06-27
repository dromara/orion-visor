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
package org.dromara.visor.module.terminal.handler.terminal.enums;

import cn.orionsec.kit.lang.utils.json.matcher.ReplacementFormatters;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 输出协议枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
@Getter
@AllArgsConstructor
public enum OutputProtocolEnum {

    // ----------------------- base ----------------------

    /**
     * 设置id
     */
    SET_ID("id", "${type}|${sessionId}"),

    /**
     * 设置会话信息
     */
    SET_INFO("if", "${type}|${info}"),

    /**
     * 已连接
     */
    CONNECTED("co", "${type}"),

    /**
     * 已关闭
     */
    CLOSED("cl", "${type}|${code}|${msg}"),

    /**
     * pong
     */
    PONG("p", "${type}"),

    /**
     * 修改大小
     */
    RESIZE("rs", "${type}|${width}|${height}"),

    // ----------------------- ssh ----------------------

    /**
     * SSH 输出
     */
    SSH_OUTPUT("o", "${type}|${body}"),

    /**
     * SSH sz 下载文件
     */
    SSH_SZ_DOWNLOAD("szd", "${type}|${paths}"),

    // ----------------------- sftp ----------------------

    /**
     * SFTP 文件列表
     */
    SFTP_LIST("ls", "${type}|${path}|${result}|${msg}|${body}"),

    /**
     * SFTP 创建文件夹
     */
    SFTP_MKDIR("mk", "${type}|${result}|${msg}"),

    /**
     * SFTP 创建文件
     */
    SFTP_TOUCH("to", "${type}|${result}|${msg}"),

    /**
     * SFTP 移动文件
     */
    SFTP_MOVE("mv", "${type}|${result}|${msg}"),

    /**
     * SFTP 删除文件
     */
    SFTP_REMOVE("rm", "${type}|${result}|${msg}"),

    /**
     * SFTP 截断文件
     */
    SFTP_TRUNCATE("tc", "${type}|${result}|${msg}"),

    /**
     * SFTP 修改文件权限
     */
    SFTP_CHMOD("chm", "${type}|${result}|${msg}"),

    /**
     * SFTP 修改文件归属
     */
    SFTP_CHOWN("cho", "${type}|${result}|${msg}"),

    /**
     * SFTP 修改文件分组
     */
    SFTP_CHGRP("chg", "${type}|${result}|${msg}"),

    /**
     * SFTP 下载文件夹展开文件
     */
    SFTP_DOWNLOAD_FLAT_DIRECTORY("df", "${type}|${currentPath}|${result}|${msg}|${body}"),

    /**
     * SFTP 获取文件内容
     */
    SFTP_GET_CONTENT("gc", "${type}|${result}|${msg}|${token}"),

    /**
     * SFTP 修改文件内容
     */
    SFTP_SET_CONTENT("sc", "${type}|${result}|${msg}|${token}"),

    // ----------------------- guacd ----------------------

    /**
     * guacd 指令
     */
    GUACD_INSTRUCTION("gi", "${type}|${instruction}"),

    ;

    private final String type;

    private final String template;

    /**
     * 格式化
     *
     * @param o o
     * @return 格式化
     */
    public String format(Object o) {
        return ReplacementFormatters.format(this.template, o);
    }

    public static OutputProtocolEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (OutputProtocolEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
