package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 主机分组 授权请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 11:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostGroupGrantRequest", description = "主机分组 授权请求对象")
public class HostGroupGrantRequest implements Serializable {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "分组id")
    private List<Long> groupIdList;

}
