package org.dromara.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户 聚合响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/19 12:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAggregateVO", description = "用户 聚合响应对象")
public class UserAggregateVO {

    @Schema(description = "用户信息")
    private SystemUserBaseVO user;

    @Schema(description = "该用户已启用的角色")
    private Collection<String> roles;

    @Schema(description = "该用户已启用的权限")
    private List<String> permissions;

    @Schema(description = "更新密码")
    private UserUpdatePasswordVO updatePassword;

    @Schema(description = "系统偏好")
    private Map<String, Object> systemPreference;

    @Schema(description = "已经提示的key")
    private List<String> tippedKeys;

}
