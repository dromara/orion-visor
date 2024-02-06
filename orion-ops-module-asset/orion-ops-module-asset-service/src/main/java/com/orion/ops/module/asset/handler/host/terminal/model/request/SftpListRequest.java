package com.orion.ops.module.asset.handler.host.terminal.model.request;

import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 列表请求 实体对象
 * <p>
 * i|eff00a1|path
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
@Schema(name = "SftpListRequest", description = "sftp 列表请求 实体对象")
public class SftpListRequest extends TerminalBasePayload {

    @Schema(description = "path")
    private String path;

}
