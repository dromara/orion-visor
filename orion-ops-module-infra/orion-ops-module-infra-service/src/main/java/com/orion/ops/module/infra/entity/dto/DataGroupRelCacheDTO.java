package com.orion.ops.module.infra.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.*;

import java.util.*;
import java.math.*;

/**
 * 数据分组关联 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupRelCacheDTO", description = "数据分组关联 缓存对象")
public class DataGroupRelCacheDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "组id")
    private Long groupId;

    @Schema(description = "引用id")
    private Long relId;

    @Schema(description = "组类型")
    private String type;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "修改人")
    private String updater;

}
