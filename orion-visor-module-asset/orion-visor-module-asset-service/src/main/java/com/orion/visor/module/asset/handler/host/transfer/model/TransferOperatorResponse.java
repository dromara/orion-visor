package com.orion.visor.module.asset.handler.host.transfer.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件操作响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileOperatorResponse", description = "文件操作响应 实体对象")
public class TransferOperatorResponse {

    @Schema(description = "channelId")
    private String channelId;

    @Schema(description = "type")
    private String type;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "传输的大小")
    private Integer currentSize;

    @Schema(description = "transferToken")
    private String transferToken;

    @Schema(description = "消息")
    private String msg;

}
