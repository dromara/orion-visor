package com.orion.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 主机密钥 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostKeyCreateRequest", description = "主机密钥 创建请求对象")
public class HostKeyCreateRequest implements Serializable {

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @Schema(description = "公钥文本")
    private String publicKey;

    @NotBlank
    @Schema(description = "私钥文本")
    private String privateKey;

    @Size(max = 512)
    @Schema(description = "密码")
    private String password;

}
