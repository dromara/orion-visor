package com.orion.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Map;

/**
 * 主机连接日志 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostConnectLogCreateRequest", description = "主机连接日志 创建请求对象")
public class HostConnectLogCreateRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "主机名称")
    private String hostName;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "状态")
    private String status;

    @Size(max = 128)
    @Schema(description = "token")
    private String token;

    @Schema(description = "拓展信息")
    private Map<String, Object> extra;

}
