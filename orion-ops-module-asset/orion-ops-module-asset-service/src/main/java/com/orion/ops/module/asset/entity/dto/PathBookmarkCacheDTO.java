package com.orion.ops.module.asset.entity.dto;

import com.orion.lang.define.cache.key.model.LongCacheIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 路径标签 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkCacheDTO", description = "路径标签 缓存对象")
public class PathBookmarkCacheDTO implements LongCacheIdModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "分组id")
    private Long groupId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "路径")
    private String path;

}
