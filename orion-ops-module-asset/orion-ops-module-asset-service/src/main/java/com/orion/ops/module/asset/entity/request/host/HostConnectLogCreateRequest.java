package com.orion.ops.module.asset.entity.request.host;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

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
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostConnectLogCreateRequest", description = "主机连接日志 创建请求对象")
public class HostConnectLogCreateRequest extends PageRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "主机id")
    private Long hostId;

    @Size(max = 128)
    @Schema(description = "token")
    private String token;

}
