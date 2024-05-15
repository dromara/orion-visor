package com.orion.visor.module.asset.entity.request.asset;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 授权资产数据 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 11:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AssetAuthorizedDataQueryRequest", description = "资产授权数据 查询请求对象")
public class AssetAuthorizedDataQueryRequest implements Serializable {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色id")
    private Long roleId;

}
