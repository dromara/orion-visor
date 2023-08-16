package com.orion.ops.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 用户 更新角色请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserUpdateRoleRequest", description = "用户 更新角色请求对象")
public class SystemUserUpdateRoleRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @Schema(description = "roleIdList")
    private List<Long> roleIdList;

}
