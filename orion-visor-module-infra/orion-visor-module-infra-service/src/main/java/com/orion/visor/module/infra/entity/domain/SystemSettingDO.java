package com.orion.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 系统设置 实体对象
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_setting", autoResultMap = true)
@Schema(name = "SystemSettingDO", description = "系统设置 实体对象")
public class SystemSettingDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "配置类型")
    @TableField("type")
    private String type;

    @Schema(description = "配置项")
    @TableField("item")
    private String item;

    @Schema(description = "配置值")
    @TableField("value")
    private String value;

}
