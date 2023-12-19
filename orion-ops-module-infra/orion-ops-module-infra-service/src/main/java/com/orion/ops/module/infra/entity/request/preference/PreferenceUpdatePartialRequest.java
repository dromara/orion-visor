package com.orion.ops.module.infra.entity.request.preference;

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
 * 用户偏好 部分更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PreferenceUpdatePartialRequest", description = "用户偏好 部分更新请求对象")
public class PreferenceUpdatePartialRequest implements Serializable {

    @NotBlank
    @Size(max = 12)
    @Schema(description = "类型")
    private String type;

    @NotEmpty
    @Schema(description = "偏好配置")
    private Map<String, Object> config;

}
