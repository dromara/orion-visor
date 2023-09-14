package com.orion.ops.module.infra.entity.request.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 角色 绑定菜单请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemRoleGrantMenuRequest", description = "角色 绑定菜单请求对象")
public class SystemRoleGrantMenuRequest implements Serializable {

    @NotNull
    @Schema(description = "roleId")
    private Long roleId;

    @NotEmpty
    @Schema(description = "菜单id集合")
    private List<Long> menuIdList;

}
