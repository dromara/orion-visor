package com.orion.ops.module.asset.handler.host.terminal.model.request;

import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 基础请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 17:46
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SftpBaseRequest", description = "sftp 基础请求 实体对象")
public class SftpBaseRequest extends TerminalBasePayload {

    @Schema(description = "path")
    private String path;

}
