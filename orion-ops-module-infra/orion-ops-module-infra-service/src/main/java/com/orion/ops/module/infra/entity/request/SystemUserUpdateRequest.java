package com.orion.ops.module.infra.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserUpdateRequest", description = "用户 更新请求对象")
public class SystemUserUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

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

    @Size(max = 500)
    @NotBlank
    @Schema(description = "头像地址")
    private String avatar;

    @Size(max = 15)
    @NotBlank
    @Schema(description = "手机号")
    private String mobile;

    @Size(max = 64)
    @NotBlank
    @Schema(description = "邮箱")
    private String email;

    @NotNull
    @Schema(description = "用户状态 0停用 1启用 2锁定")
    private Integer status;

    @NotNull
    @Schema(description = "最后登录时间")
    private Date lastLoginTime;

}
