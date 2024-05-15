package com.orion.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 数据权限 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "data_permission", autoResultMap = true)
@Schema(name = "DataPermissionDO", description = "数据权限 实体对象")
public class DataPermissionDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "角色id")
    @TableField("role_id")
    private Long roleId;

    @Schema(description = "引用id")
    @TableField("rel_id")
    private Long relId;

    @Schema(description = "数据类型")
    @TableField("type")
    private String type;

}
