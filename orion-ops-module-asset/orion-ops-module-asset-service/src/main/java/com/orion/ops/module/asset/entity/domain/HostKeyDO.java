package com.orion.ops.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 主机秘钥 实体对象
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
@TableName(value = "host_key", autoResultMap = true)
@Schema(name = "HostKeyDO", description = "主机秘钥 实体对象")
public class HostKeyDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "公钥文本")
    @TableField("public_key")
    private String publicKey;

    @Schema(description = "私钥文本")
    @TableField("private_key")
    private String privateKey;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

}
