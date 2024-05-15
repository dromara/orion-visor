package com.orion.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 数据拓展信息 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "data_extra", autoResultMap = true)
@Schema(name = "DataExtraDO", description = "数据拓展信息 实体对象")
public class DataExtraDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "数据id")
    @TableField("rel_id")
    private Long relId;

    @Schema(description = "数据类型")
    @TableField("type")
    private String type;

    @Schema(description = "配置项")
    @TableField("item")
    private String item;

    @Schema(description = "配置值")
    @TableField("value")
    private String value;

}
