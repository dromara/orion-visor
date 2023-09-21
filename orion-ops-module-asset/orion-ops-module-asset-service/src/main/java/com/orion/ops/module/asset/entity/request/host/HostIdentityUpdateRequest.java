package com.orion.ops.module.asset.entity.request.host;

import com.orion.ops.framework.common.security.UpdatePasswordAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 主机身份 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostIdentityUpdateRequest", description = "主机身份 更新请求对象")
public class HostIdentityUpdateRequest implements UpdatePasswordAction {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "用户名")
    private String username;

    @Size(max = 512)
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "秘钥id")
    private Long keyId;

    @Schema(description = "是否使用新密码")
    private Boolean useNewPassword;

}
