package com.orion.ops.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserCreateRequest", description = "用户 创建请求对象")
public class SystemUserCreateRequest implements Serializable {

    @Size(max = 32)
    @NotBlank
    @Schema(description = "用户名")
    private String username;

    @Size(max = 64)
    @NotBlank
    @Schema(description = "密码")
    private String password;

    @Size(max = 16)
    @NotBlank
    @Schema(description = "花名")
    private String nickname;

    @Size(max = 15)
    @Schema(description = "手机号")
    private String mobile;

    @Size(max = 64)
    @Schema(description = "邮箱")
    private String email;

}
