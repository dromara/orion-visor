package com.orion.visor.module.asset.handler.host.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sftp 文件备份参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 23:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SftpFileBackupParams {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 时间戳
     */
    private Long timestamp;

}
