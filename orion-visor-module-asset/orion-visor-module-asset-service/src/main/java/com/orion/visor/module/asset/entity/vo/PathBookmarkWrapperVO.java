package com.orion.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 路径标签组合 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/24 17:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkWrapperVO", description = "路径标签组合 视图响应对象")
public class PathBookmarkWrapperVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组")
    private List<PathBookmarkGroupVO> groups;

    @Schema(description = "未分组的路径标签")
    private List<PathBookmarkVO> ungroupedItems;

}