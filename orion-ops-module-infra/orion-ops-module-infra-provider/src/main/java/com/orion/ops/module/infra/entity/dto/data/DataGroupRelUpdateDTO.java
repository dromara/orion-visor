package com.orion.ops.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
@Schema(name = "DataGroupRelUpdateDTO", description = "数据分组关联 更新请求对象")
public class DataGroupRelUpdateDTO implements Serializable {

    @NotNull
    @Schema(description = "组id")
    private Long groupId;

    @Schema(description = "引用id")
    private List<Long> relIdList;

}
