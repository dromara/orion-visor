package com.orion.ops.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;
import java.math.*;

/**
 * 数据分组关联 实体对象
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
@TableName(value = "data_group_rel", autoResultMap = true)
@Schema(name = "DataGroupRelDO", description = "数据分组关联 实体对象")
public class DataGroupRelDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "组id")
    @TableField("group_id")
    private Long groupId;

    @Schema(description = "引用id")
    @TableField("rel_id")
    private Long relId;

    @Schema(description = "组类型")
    @TableField("type")
    private String type;

}
