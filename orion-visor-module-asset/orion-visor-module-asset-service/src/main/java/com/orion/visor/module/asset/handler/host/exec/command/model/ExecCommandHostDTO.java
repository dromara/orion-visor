package com.orion.visor.module.asset.handler.host.exec.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量执行启动主机对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecCommandHostDTO {

    /**
     * hostLogId
     */
    private Long hostLogId;

    /**
     * hostId
     */
    private Long hostId;

    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 主机地址
     */
    private String hostAddress;

    /**
     * 日志文件路径
     */
    private String logPath;

    /**
     * 脚本路径
     */
    private String scriptPath;

    /**
     * 执行命令
     */
    private String command;

    /**
     * 主机用户
     */
    private String username;

    /**
     * 命令编码
     */
    private String charset;

    /**
     * 文件名称编码
     */
    private String fileNameCharset;

    /**
     * 文件内容编码
     */
    private String fileContentCharset;

}
