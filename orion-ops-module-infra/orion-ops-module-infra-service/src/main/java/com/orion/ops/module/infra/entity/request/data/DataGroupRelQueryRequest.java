package com.orion.ops.module.infra.entity.request.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.*;
import java.math.*;

/**
 * 数据分组关联 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DataGroupRelQueryRequest", description = "数据分组关联 查询请求对象")
public class DataGroupRelQueryRequest extends PageRequest {

    @Schema(description = "搜索")
    private String searchValue;

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
