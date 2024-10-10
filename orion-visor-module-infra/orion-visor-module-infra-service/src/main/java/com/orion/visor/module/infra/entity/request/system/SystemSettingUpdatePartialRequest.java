package com.orion.visor.module.infra.entity.request.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;

/**
 * 系统设置部分 更新请求对象
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemSettingUpdatePartialRequest", description = "系统设置部分 更新请求对象")
public class SystemSettingUpdatePartialRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 16)
    @Schema(description = "配置类型")
    private String type;

    @NotEmpty
    @Schema(description = "配置")
    private Map<String, Object> settings;

}
