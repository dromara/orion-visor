package com.orion.visor.module.infra.entity.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户状态检查 业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/14 21:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserAuthDTO", description = "用户认证 业务对象")
public class SystemUserAuthDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "密码是否正确")
    private Boolean passRight;

    @Schema(description = "认证是否通过")
    private Boolean authed;

    @Schema(description = "错误信息")
    private String errorMessage;

}
