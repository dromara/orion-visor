package com.orion.visor.module.infra.entity.dto;

import com.orion.lang.define.cache.key.model.LongCacheIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户信息 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserInfoDTO", description = "用户信息 缓存对象")
public class UserInfoDTO implements LongCacheIdModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "用户状态 0停用 1启用")
    private Integer status;

}
