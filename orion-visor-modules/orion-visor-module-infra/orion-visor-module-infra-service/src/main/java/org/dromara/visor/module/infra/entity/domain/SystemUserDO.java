/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.util.Date;

/**
 * 用户 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_user", autoResultMap = true)
@Schema(name = "SystemUserDO", description = "用户 实体对象")
public class SystemUserDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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

    @Schema(description = "修改密码状态")
    @TableField("update_password_status")
    private Integer updatePasswordStatus;

    @Schema(description = "修改密码原因")
    @TableField("update_password_reason")
    private String updatePasswordReason;

    @Schema(description = "修改密码时间")
    @TableField("update_password_time")
    private Date updatePasswordTime;

    @Schema(description = "最后登录时间")
    @TableField("last_login_time")
    private Date lastLoginTime;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

}
