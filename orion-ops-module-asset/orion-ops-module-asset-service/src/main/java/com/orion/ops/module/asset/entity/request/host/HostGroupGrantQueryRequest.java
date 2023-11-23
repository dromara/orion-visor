package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主机分组 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 11:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostGroupQueryRequest", description = "主机分组 查询请求对象")
public class HostGroupGrantQueryRequest implements Serializable {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色id")
    private Long roleId;

}
