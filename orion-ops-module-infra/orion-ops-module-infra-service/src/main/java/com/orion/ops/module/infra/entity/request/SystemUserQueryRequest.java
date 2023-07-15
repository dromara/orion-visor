package com.orion.ops.module.infra.entity.request;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 用户 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
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

    @Size(max = 32)
    @Schema(description = "用户名")
    private String username;

    @Size(max = 64)
    @Schema(description = "密码")
    private String password;

    @Size(max = 16)
    @Schema(description = "花名")
    private String nickname;

    @Size(max = 500)
    @Schema(description = "头像地址")
    private String avatar;

    @Size(max = 15)
    @Schema(description = "手机号")
    private String mobile;

    @Size(max = 64)
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户状态 0正常 1停用 2锁定")
    private Integer status;

    @Schema(description = "最后登录时间")
    private Date lastLoginTime;

}
