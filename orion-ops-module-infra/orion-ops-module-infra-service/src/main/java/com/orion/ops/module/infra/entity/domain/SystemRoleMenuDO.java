package com.orion.ops.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

/**
 * 角色菜单关联 实体对象
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
@TableName(value = "system_role_menu", autoResultMap = true)
@Schema(name = "SystemRoleMenuDO", description = "角色菜单关联 实体对象")
public class SystemRoleMenuDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "角色id")
    @TableField("role_id")
    private Long roleId;

    @Schema(description = "菜单id")
    @TableField("menu_id")
    private Long menuId;

}
