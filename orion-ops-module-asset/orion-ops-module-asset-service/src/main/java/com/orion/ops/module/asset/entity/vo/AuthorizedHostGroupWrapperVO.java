package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 已授权的主机分组 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/30 21:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthorizedHostGroupWrapperVO", description = "已授权的主机分组 视图响应对象")
public class AuthorizedHostGroupWrapperVO {

    @Schema(description = "授权的主机分组")
    private List<HostGroupTreeVO> groupTree;

    @Schema(description = "授权的主机列表")
    private List<HostVO> hostList;

}
