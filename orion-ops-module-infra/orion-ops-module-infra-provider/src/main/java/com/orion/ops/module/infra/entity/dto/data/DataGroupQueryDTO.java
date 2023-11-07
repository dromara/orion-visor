package com.orion.ops.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;
import java.math.*;

/**
 * 数据分组 查询请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupQueryDTO", description = "数据分组 查询请求业务对象")
public class DataGroupQueryDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @Size(max = 32)
    @Schema(description = "组名称")
    private String name;

    @Size(max = 16)
    @Schema(description = "组类型")
    private String type;

    @Schema(description = "排序")
    private Integer sort;

}
