package com.orion.visor.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 主机身份 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "host_identity", autoResultMap = true)
@Schema(name = "HostIdentityDO", description = "主机身份 实体对象")
public class HostIdentityDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "类型")
    @TableField("type")
    private String type;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "用户密码")
    @TableField("password")
    private String password;

    @Schema(description = "秘钥id")
    @TableField("key_id")
    private Long keyId;

}
