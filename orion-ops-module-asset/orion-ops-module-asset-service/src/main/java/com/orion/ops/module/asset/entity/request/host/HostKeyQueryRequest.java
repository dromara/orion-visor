package com.orion.ops.module.asset.entity.request.host;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 主机秘钥 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostKeyQueryRequest", description = "主机秘钥 查询请求对象")
public class HostKeyQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @Size(max = 65535)
    @Schema(description = "公钥文本")
    private String publicKey;

    @Size(max = 65535)
    @Schema(description = "私钥文本")
    private String privateKey;

    @Size(max = 512)
    @Schema(description = "密码")
    private String password;

}
