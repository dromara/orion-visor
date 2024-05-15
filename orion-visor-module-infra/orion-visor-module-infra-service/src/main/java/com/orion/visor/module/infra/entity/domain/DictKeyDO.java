package com.orion.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 字典配置项 实体对象
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
@TableName(value = "dict_key", autoResultMap = true)
@Schema(name = "DictKeyDO", description = "字典配置项 实体对象")
public class DictKeyDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "配置项")
    @TableField("key_name")
    private String keyName;

    @Schema(description = "配置值定义")
    @TableField("value_type")
    private String valueType;

    @Schema(description = "额外配置定义")
    @TableField("extra_schema")
    private String extraSchema;

    @Schema(description = "配置描述")
    @TableField("description")
    private String description;

}
