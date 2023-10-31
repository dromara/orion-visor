package com.orion.ops.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录响应
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserVO", description = "用户 视图响应对象")
public class UserLoginVO {

    @Schema(description = "登录 token")
    private String token;

}
