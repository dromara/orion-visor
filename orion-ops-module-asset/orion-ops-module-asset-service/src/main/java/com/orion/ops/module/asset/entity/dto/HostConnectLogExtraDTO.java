package com.orion.ops.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机连接日志拓展信息对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-3-12 23:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostConnectLogExtraDTO", description = "主机连接日志拓展信息对象")
public class HostConnectLogExtraDTO {

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "主机名称")
    private String hostName;

    @Schema(description = "连接类型")
    private String connectType;

    @Schema(description = "traceId")
    private String traceId;

    @Schema(description = "channelId")
    private String channelId;

    @Schema(description = "sessionId")
    private String sessionId;

    @Schema(description = "请求地址")
    private String address;

    @Schema(description = "请求位置")
    private String location;

    @Schema(description = "ua")
    private String userAgent;

    @Schema(description = "错误信息")
    private String errorMessage;

}
