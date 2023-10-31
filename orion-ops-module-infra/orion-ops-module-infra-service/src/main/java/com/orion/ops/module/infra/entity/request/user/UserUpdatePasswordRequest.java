package com.orion.ops.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 修改密码请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 12:19
 */
@Data
@Schema(name = "UserUpdatePasswordRequest", description = "修改密码请求")
public class UserUpdatePasswordRequest {

    @NotEmpty
    @Schema(description = "原密码")
    private String beforePassword;

    @NotEmpty
    @Schema(description = "新密码")
    private String password;

}
