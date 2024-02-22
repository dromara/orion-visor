package com.orion.ops.module.asset.handler.host.terminal.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 下载文件夹展开文件 实体对象
 * <p>
 * i|eff00a1|currentPath|path
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/6 13:31
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SftpDownloadDirectoryFlatRequest", description = "sftp 下载文件夹展开文件 实体对象")
public class SftpDownloadFlatDirectoryRequest extends SftpBaseRequest {

    @Schema(description = "当前路径")
    private String currentPath;

}
