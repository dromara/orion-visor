package com.orion.ops.module.asset.handler.host.terminal.enums;

import com.orion.lang.utils.json.matcher.ReplacementFormatters;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 输出操作类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
@Getter
@AllArgsConstructor
public enum OutputTypeEnum {

    /**
     * 主机连接检查
     */
    CHECK("ck", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * 主机连接
     */
    CONNECT("co", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * 关闭连接
     */
    CLOSE("cl", "${type}|${sessionId}|${msg}"),

    /**
     * pong
     */
    PONG("p", "${type}"),

    /**
     * SSH 输出
     */
    SSH_OUTPUT("o", "${type}|${sessionId}|${body}"),

    /**
     * SFTP 文件列表
     */
    SFTP_LIST("ls", "${type}|${sessionId}|${path}|${result}|${body}"),

    /**
     * SFTP 创建文件夹
     */
    SFTP_MKDIR("mk", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * SFTP 创建文件
     */
    SFTP_TOUCH("to", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * SFTP 移动文件
     */
    SFTP_MOVE("mv", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * SFTP 删除文件
     */
    SFTP_REMOVE("rm", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * SFTP 截断文件
     */
    SFTP_TRUNCATE("tc", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * SFTP 修改文件权限
     */
    SFTP_CHMOD("cm", "${type}|${sessionId}|${result}|${msg}"),

    /**
     * SFTP 下载文件夹 flat
     */
    SFTP_DOWNLOAD_DIRECTORY_FLAT("df", "${type}|${sessionId}|${currentPath}|${body}"),

    /**
     * SFTP 获取文件内容
     */
    SFTP_GET_CONTENT("gc", "${type}|${sessionId}|${path}|${result}|${content}"),

    /**
     * SFTP 修改文件内容
     */
    SFTP_SET_CONTENT("sc", "${type}|${sessionId}|${result}|${msg}"),

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

    public static OutputTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (OutputTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
