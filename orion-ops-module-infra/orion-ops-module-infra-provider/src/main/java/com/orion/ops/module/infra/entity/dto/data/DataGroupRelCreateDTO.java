package com.orion.ops.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;
import java.math.*;

/**
 * 数据分组关联 创建请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupRelCreateDTO", description = "数据分组关联 创建请求业务对象")
public class DataGroupRelCreateDTO implements Serializable {

    @NotNull
    @Schema(description = "组id")
    private Long groupId;

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

    @NotBlank
    @Size(max = 16)
    @Schema(description = "组类型")
    private String type;

    @NotNull
    @Schema(description = "排序")
    private Integer sort;

}
