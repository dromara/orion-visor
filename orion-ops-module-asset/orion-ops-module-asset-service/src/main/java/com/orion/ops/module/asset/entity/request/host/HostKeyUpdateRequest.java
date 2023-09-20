package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 主机秘钥 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostKeyUpdateRequest", description = "主机秘钥 更新请求对象")
public class HostKeyUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @Size(max = 65535)
    @Schema(description = "公钥文本")
    private String publicKey;

    @NotBlank
    @Size(max = 65535)
    @Schema(description = "私钥文本")
    private String privateKey;

    @Size(max = 512)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "是否使用新密码")
    private Boolean useNewPassword;

}
