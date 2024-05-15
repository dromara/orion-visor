package com.orion.visor.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户 更新状态请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserUpdateStatusRequest", description = "用户 更新状态请求对象")
public class SystemUserUpdateStatusRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "用户状态 0停用 1启用")
    private Integer status;

}
