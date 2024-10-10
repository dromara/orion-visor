package com.orion.visor.module.infra.entity.request.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 系统设置 更新请求对象
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemSettingUpdateRequest", description = "系统设置 更新请求对象")
public class SystemSettingUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 16)
    @Schema(description = "配置类型")
    private String type;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "配置项")
    private String item;

    @Schema(description = "配置值")
    private Object value;

}
