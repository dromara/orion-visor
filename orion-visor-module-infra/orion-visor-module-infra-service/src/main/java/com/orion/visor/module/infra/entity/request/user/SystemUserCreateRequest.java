package com.orion.visor.module.infra.entity.request.user;

import com.orion.visor.framework.common.constant.ValidConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @NotBlank
    @Size(max = 32)
    @Pattern(regexp = ValidConst.USERNAME_4_32_PATTERN, message = ValidConst.USERNAME_4_32_MESSAGE)
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "密码")
    private String password;

    @NotBlank
    @Size(max = 16)
    @Schema(description = "花名")
    private String nickname;

    @Size(max = 15)
    @Schema(description = "手机号")
    private String mobile;

    @Size(max = 64)
    @Schema(description = "邮箱")
    private String email;

}
