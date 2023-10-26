package com.orion.ops.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 字典配置值 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "dict_value", autoResultMap = true)
@Schema(name = "DictValueDO", description = "字典配置值 实体对象")
public class DictValueDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "配置项id")
    @TableField("key_id")
    private Long keyId;

    @Schema(description = "配置项")
    @TableField("key_name")
    private String keyName;

    @Schema(description = "配置值")
    @TableField("value")
    private String value;

    @Schema(description = "配置描述")
    @TableField("label")
    private String label;

    @Schema(description = "额外参数")
    @TableField("extra")
    private String extra;

    @Schema(description = "排序")
    @TableField("sort")
    private Integer sort;

}
