package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 命令片段组合 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/24 17:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommandSnippetWrapperVO", description = "命令片段组合 视图响应对象")
public class CommandSnippetWrapperVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组")
    private List<CommandSnippetGroupVO> groups;

    @Schema(description = "未分组的命令片段")
    private List<CommandSnippetVO> ungroupedItems;

}