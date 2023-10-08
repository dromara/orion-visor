package com.orion.ops.module.infra.handler.preference.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提示偏好模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipsPreferenceModel implements PreferenceModel {

    @Schema(description = "是否提示过系统偏好设置模态框")
    private Boolean tippedSystemPreferenceModal;

}
