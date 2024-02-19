package com.orion.ops.module.asset.handler.host.terminal.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * sftp 获取内容响应 实体对象
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
@Schema(name = "SftpGetContentResponse", description = "sftp 获取内容响应 实体对象")
public class SftpGetContentResponse extends SftpBaseResponse {

    @Schema(description = "path")
    private String path;

    @Schema(description = "content")
    private String content;

}
