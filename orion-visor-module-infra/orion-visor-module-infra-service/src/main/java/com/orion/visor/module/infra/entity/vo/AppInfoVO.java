package com.orion.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 应用信息 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-18 10:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AppInfoVO", description = "应用信息 视图响应对象")
public class AppInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "系统版本")
    private String version;

    @Schema(description = "机器码")
    private String uuid;

}
