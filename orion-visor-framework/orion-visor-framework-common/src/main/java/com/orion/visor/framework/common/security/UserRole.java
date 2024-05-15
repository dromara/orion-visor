package com.orion.visor.framework.common.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 用户角色
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/20 23:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(name = "UserRole", description = "用户角色")
public class UserRole {

    @EqualsAndHashCode.Include
    @Schema(description = "id")
    private Long id;

    @Schema(description = "角色")
    private String code;

}
