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
 * 用户 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_user", autoResultMap = true)
@Schema(name = "SystemUserDO", description = "用户 实体对象")
public class SystemUserDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "花名")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "头像地址")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "手机号")
    @TableField("mobile")
    private String mobile;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "用户状态 0停用 1启用")
    @TableField("status")
    private Integer status;

    @Schema(description = "最后登录时间")
    @TableField("last_login_time")
    private Date lastLoginTime;

}
