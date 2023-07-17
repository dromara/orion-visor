package com.orion.ops.module.infra.entity.request.role;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 角色 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemRoleQueryRequest", description = "角色 查询请求对象")
public class SystemRoleQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "角色名称")
    private String name;

    @Size(max = 32)
    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "状态 0停用 1启用")
    private Integer status;

}
