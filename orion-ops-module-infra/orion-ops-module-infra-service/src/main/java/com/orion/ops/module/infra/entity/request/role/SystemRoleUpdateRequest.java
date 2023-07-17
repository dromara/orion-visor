package com.orion.ops.module.infra.entity.request.role;

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
 * 角色 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemRoleUpdateRequest", description = "角色 更新请求对象")
public class SystemRoleUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @NotBlank
    @Schema(description = "角色名称")
    private String name;

}
