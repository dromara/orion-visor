package com.orion.ops.module.asset.entity.request.exec;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 执行模板 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecTemplateQueryRequest", description = "执行模板 查询请求对象")
public class ExecTemplateQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @Schema(description = "命令")
    private String command;

}
