package com.orion.ops.module.asset.entity.request.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 命令片段 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommandSnippetCreateRequest", description = "命令片段 创建请求对象")
public class CommandSnippetCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组id")
    private Long groupId;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @NotBlank
    @Schema(description = "代码片段")
    private String command;

}
