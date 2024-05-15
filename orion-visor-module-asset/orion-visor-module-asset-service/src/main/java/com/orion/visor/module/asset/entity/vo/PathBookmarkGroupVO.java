package com.orion.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 路径标签分组 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkGroupVO", description = "路径标签分组 视图响应对象")
public class PathBookmarkGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "分组名称")
    private String name;

    @Schema(description = "路径标签列表")
    private List<PathBookmarkVO> items;

}
