package com.orion.ops.module.asset.entity.request.host;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 主机身份 查询请求对象
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
@Schema(name = "HostIdentityQueryRequest", description = "主机身份 查询请求对象")
public class HostIdentityQueryRequest extends PageRequest {

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "id")
    private Long id;

    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @Size(max = 12)
    @Schema(description = "类型")
    private String type;

    @Size(max = 128)
    @Schema(description = "用户名")
    private String username;

    @Size(max = 512)
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "秘钥id")
    private Long keyId;

}
