package com.orion.visor.module.infra.entity.request.preference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户偏好 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PreferenceUpdateRequest", description = "用户偏好 更新请求对象")
public class PreferenceUpdateRequest implements Serializable {

    @NotBlank
    @Size(max = 12)
    @Schema(description = "类型")
    private String type;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "偏好配置")
    private String item;

    @Schema(description = "偏好配置")
    private Object value;

}
