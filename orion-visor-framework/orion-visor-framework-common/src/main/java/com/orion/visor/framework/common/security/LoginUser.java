package com.orion.visor.framework.common.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 当前登录用户
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 18:36
 */
@Data
@Schema(name = "LoginUser", description = "当前登录用户对象")
public class LoginUser {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "用户状态")
    private Integer status;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "登录时间戳")
    private Long timestamp;

    @Schema(description = "角色")
    private List<UserRole> roles;

}
