package com.orion.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 主机配置 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/11 17:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostConfigVO", description = "主机配置 视图响应对象")
public class HostConfigVO {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "type")
    private String type;

    @Schema(description = "config")
    private Map<String, Object> config;

}
