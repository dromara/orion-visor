package com.orion.ops.module.infra.entity.request.user;

import com.orion.ops.framework.desensitize.core.annotation.Desensitize;
import com.orion.ops.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登陆请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/13 22:16
 */
@Data
@DesensitizeObject
@Schema(name = "UserLoginRequest", description = "登陆请求")
public class UserLoginRequest {

    @NotEmpty
    @Schema(description = "用户名")
    private String username;

    @Desensitize(toEmpty = true)
    @NotEmpty
    @Schema(description = "密码")
    private String password;

}
