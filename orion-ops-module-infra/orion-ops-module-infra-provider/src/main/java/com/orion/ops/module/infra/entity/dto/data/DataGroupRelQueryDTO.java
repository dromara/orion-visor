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
 * 数据分组关联 查询请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupRelQueryDTO", description = "数据分组关联 查询请求业务对象")
public class DataGroupRelQueryDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "组id")
    private Long groupId;

    @Schema(description = "引用id")
    private Long relId;

    @Size(max = 16)
    @Schema(description = "组类型")
    private String type;

    @Schema(description = "排序")
    private Integer sort;

}
