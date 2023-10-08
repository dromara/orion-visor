package com.orion.ops.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 用户基本信息 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/19 12:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserCollectInfoVO", description = "用户聚合信息 视图响应对象")
public class UserCollectInfoVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "系统偏好")
    private Map<String, Object> systemPreference;

}
