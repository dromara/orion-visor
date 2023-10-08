package com.orion.ops.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户偏好 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PreferenceVO", description = "用户偏好 视图响应对象")
public class PreferenceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "偏好配置")
    private Map<String, Object> config;

}
