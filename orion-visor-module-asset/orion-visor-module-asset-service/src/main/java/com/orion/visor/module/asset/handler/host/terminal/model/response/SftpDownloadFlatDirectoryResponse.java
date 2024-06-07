package com.orion.visor.module.asset.handler.host.terminal.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 下载文件夹展开文件 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/6 16:20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SftpDownloadFlatDirectoryResponse extends SftpBaseResponse {

    /**
     * currentPath
     */
    private String currentPath;

    /**
     * body
     */
    private String body;

}
