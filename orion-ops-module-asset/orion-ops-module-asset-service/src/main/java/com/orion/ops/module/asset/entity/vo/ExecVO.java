package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 批量执行 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 14:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecVO", description = "批量执行 视图响应对象")
public class ExecVO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "主机 id 映射 host:id")
    private Map<String, Long> hostIdRel;

}
