package com.orion.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 数据分组 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "data_group", autoResultMap = true)
@Schema(name = "DataGroupDO", description = "数据分组 实体对象")
public class DataGroupDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "父id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "组名称")
    @TableField("name")
    private String name;

    @Schema(description = "组类型")
    @TableField("type")
    private String type;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "排序")
    @TableField("sort")
    private Integer sort;

}
