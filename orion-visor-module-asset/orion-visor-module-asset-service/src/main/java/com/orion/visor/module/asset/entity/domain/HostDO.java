package com.orion.visor.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 主机 实体对象
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
@TableName(value = "host", autoResultMap = true)
@Schema(name = "HostDO", description = "主机 实体对象")
public class HostDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "主机类型")
    @TableField("type")
    private String type;

    @Schema(description = "主机名称")
    @TableField("name")
    private String name;

    @Schema(description = "主机编码")
    @TableField("code")
    private String code;

    @Schema(description = "主机地址")
    @TableField("address")
    private String address;

    @Schema(description = "主机端口")
    @TableField("port")
    private Integer port;

    @Schema(description = "主机状态")
    @TableField("status")
    private String status;

    @Schema(description = "主机配置")
    @TableField("config")
    private String config;

}
