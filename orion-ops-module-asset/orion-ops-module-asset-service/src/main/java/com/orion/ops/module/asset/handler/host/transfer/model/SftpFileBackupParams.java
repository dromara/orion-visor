package com.orion.ops.module.asset.handler.host.transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "SftpFileBackupParams", description = "sftp 文件备份参数")
public class SftpFileBackupParams {

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "时间戳")
    private Long timestamp;

}
