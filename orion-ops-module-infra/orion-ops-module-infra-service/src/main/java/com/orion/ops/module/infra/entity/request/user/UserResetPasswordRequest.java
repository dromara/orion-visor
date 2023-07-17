package com.orion.ops.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 重置密码请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 12:19
 */
@Data
@Schema(name = "UserResetPasswordRequest", description = "重置密码请求")
public class UserResetPasswordRequest {

    @Schema(description = "id")
    private Long id;

    @NotEmpty
    @Schema(description = "密码")
    private String password;

}
