package com.orion.visor.module.asset.handler.host.transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件操作请求 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileOperatorRequest", description = "文件操作请求 实体对象")
public class TransferOperatorRequest {

    @Schema(description = "上传路径")
    private String path;

    @Schema(description = "type")
    private String type;

    @Schema(description = "主机id")
    private Long hostId;

}
