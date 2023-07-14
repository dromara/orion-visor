package com.orion.ops.module.infra.entity.request;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

/**
 * 用户 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-14 10:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemUserQueryRequest", description = "用户 查询请求对象")
public class SystemUserQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户状态 0正常 1停用 2锁定")
    private Byte status;

    @Schema(description = "最后登录时间")
    private Date lastLoginTime;

}
