package com.orion.visor.module.asset.handler.host.terminal.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 修改文件权限 实体对象
 * <p>
 * i|eff00a1|path|mod
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
@Schema(name = "SftpChangeModRequest", description = "sftp 修改文件权限 实体对象")
public class SftpChangeModRequest extends SftpBaseRequest {

    @Schema(description = "10进制的8进制 权限")
    private Integer mod;

}
