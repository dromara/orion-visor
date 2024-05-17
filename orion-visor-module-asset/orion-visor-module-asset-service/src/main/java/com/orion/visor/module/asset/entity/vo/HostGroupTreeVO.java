package com.orion.visor.module.asset.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orion.visor.framework.common.entity.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 主机密钥 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostGroupTreeVO", description = "主机密钥 视图响应对象")
public class HostGroupTreeVO implements TreeNode<HostGroupTreeVO>, Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("key")
    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @JsonProperty("title")
    @Schema(description = "组名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "子节点")
    private List<HostGroupTreeVO> children;

}
