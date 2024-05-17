package com.orion.visor.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登录请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 22:16
 */
@Data
@Schema(name = "UserLoginRequest", description = "登录请求")
public class UserLoginRequest {

    @NotEmpty
    @Schema(description = "用户名")
    private String username;

    @NotEmpty
    @Schema(description = "密码")
    private String password;

}
