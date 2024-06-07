package com.orion.visor.module.asset.handler.host.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机终端连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 23:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalConfig {

    /**
     * logId
     */
    private Long logId;

    /**
     * 主机id
     */
    private Long hostId;

    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 主机地址
     */
    private String address;

    /**
     * cols
     */
    private Integer cols;

    /**
     * rows
     */
    private Integer rows;

    /**
     * SSH输出编码
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
