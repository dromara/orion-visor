package org.dromara.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户更新密码 响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/16 11:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserUpdatePasswordVO", description = "用户更新密码 响应对象")
public class UserUpdatePasswordVO {

    @Schema(description = "修改密码状态")
    private Integer updatePasswordStatus;

    @Schema(description = "修改密码原因")
    private String updatePasswordReason;

}
