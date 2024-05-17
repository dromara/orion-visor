package com.orion.visor.module.infra.entity.dto;

import com.orion.lang.define.cache.key.model.LongCacheIdModel;
import com.orion.visor.framework.common.entity.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 数据分组 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupCacheDTO", description = "数据分组 缓存对象")
public class DataGroupCacheDTO implements TreeNode<DataGroupCacheDTO>, LongCacheIdModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @Schema(description = "组名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "子节点")
    private List<DataGroupCacheDTO> children;

}
