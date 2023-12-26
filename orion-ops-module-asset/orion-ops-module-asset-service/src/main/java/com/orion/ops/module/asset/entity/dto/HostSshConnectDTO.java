package com.orion.ops.module.asset.entity.dto;

import com.orion.ops.module.asset.entity.domain.HostKeyDO;
import com.orion.ops.module.asset.enums.HostSshAuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 15:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostSshConnectDTO", description = "主机连接参数")
public class HostSshConnectDTO {

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "主机地址")
    private String address;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "认证方式")
    private HostSshAuthTypeEnum authType;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "主机秘钥")
    private HostKeyDO key;

    // @Schema(description = "")
    // private ;

}
