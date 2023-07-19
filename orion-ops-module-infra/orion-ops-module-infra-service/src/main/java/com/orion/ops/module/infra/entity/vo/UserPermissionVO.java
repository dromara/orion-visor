package com.orion.ops.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户权限 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/19 12:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserPermissionVO", description = "用户权限 视图响应对象")
public class UserPermissionVO {

    @Schema(description = "用户基本信息")
    private UserBaseInfoVO user;

    @Schema(description = "该用户已启用的角色")
    private List<String> roles;

    @Schema(description = "该用户已启用的权限")
    private List<String> permissions;

}
