package com.orion.ops.module.asset.handler.host.transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 文件操作请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:01
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileOperatorRequest", description = "文件操作请求 实体对象")
public class TransferOperatorRequest {

    @Schema(description = "上传路径")
    private String path;

    @Schema(description = "type")
    private String type;

    @Schema(description = "fileId")
    private String fileId;

    @Schema(description = "主机id")
    private Long hostId;

}
