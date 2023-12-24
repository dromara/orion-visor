package com.orion.ops.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 主机配置 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "host_config", autoResultMap = true)
@Schema(name = "HostConfigDO", description = "主机配置 实体对象")
public class HostConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "主机id")
    @TableField("host_id")
    private Long hostId;

    @Schema(description = "配置类型")
    @TableField("type")
    private String type;

    @Schema(description = "状态 0停用 1启用")
    @TableField("status")
    private Integer status;

    @Schema(description = "配置详情")
    @TableField("config")
    private String config;

    @Schema(description = "配置版本号")
    @TableField("version")
    @Version
    private Integer version;

}
