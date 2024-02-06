package com.orion.ops.module.asset.handler.host.terminal.model.request;

import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 修改文件权限 实体对象
 * <p>
 * i|eff00a1|path|mode
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
@Schema(name = "SftpChangeModeRequest", description = "sftp 修改文件权限 实体对象")
public class SftpChangeModeRequest extends TerminalBasePayload {

    @Schema(description = "path")
    private String path;

    @Schema(description = "权限")
    private String mode;

}
