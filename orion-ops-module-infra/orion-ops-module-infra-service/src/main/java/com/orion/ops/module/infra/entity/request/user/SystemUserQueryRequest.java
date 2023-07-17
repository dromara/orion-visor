package com.orion.ops.module.infra.entity.request.user;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 用户 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemUserQueryRequest", description = "用户 查询请求对象")
public class SystemUserQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "用户名")
    private String username;

    @Size(max = 16)
    @Schema(description = "花名")
    private String nickname;

    @Size(max = 15)
    @Schema(description = "手机号")
    private String mobile;

    @Size(max = 64)
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户状态 0停用 1启用 2锁定")
    private Integer status;

}
