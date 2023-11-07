package com.orion.ops.module.infra.entity.request.data;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 数据分组关联 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupRelUpdateRequest", description = "数据分组关联 更新请求对象")
public class DataGroupRelUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

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
