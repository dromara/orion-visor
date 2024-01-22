package com.orion.ops.module.asset.entity.request.command;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 命令片段 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "CommandSnippetQueryRequest", description = "命令片段 查询请求对象")
public class CommandSnippetQueryRequest extends PageRequest {

    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

}
