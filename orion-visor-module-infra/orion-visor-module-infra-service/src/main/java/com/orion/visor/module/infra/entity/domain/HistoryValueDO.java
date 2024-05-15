package com.orion.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

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
@TableName(value = "history_value", autoResultMap = true)
@Schema(name = "HistoryValueDO", description = "字典配置项 实体对象")
public class HistoryValueDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "引用id")
    @TableField("rel_id")
    private Long relId;

    @Schema(description = "类型")
    @TableField("type")
    private String type;

    @Schema(description = "修改前")
    @TableField("before_value")
    private String beforeValue;

    @Schema(description = "修改后")
    @TableField("after_value")
    private String afterValue;

    @Schema(description = "修改时间")
    @TableField(exist = false)
    private Date updateTime;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

}
