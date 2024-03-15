package com.orion.ops.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 命令执行参数 schema 对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/15 14:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecParameterSchemaDTO", description = "命令执行参数 schema 对象")
public class ExecParameterSchemaDTO {

    @Schema(description = "参数名称")
    private String name;

    @Schema(description = "参数描述")
    private String desc;

    @Schema(description = "默认值")
    private Object defaultValue;

    @Schema(description = "值")
    private Object value;

}
