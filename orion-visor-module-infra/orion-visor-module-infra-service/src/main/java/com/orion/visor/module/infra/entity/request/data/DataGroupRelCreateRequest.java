package com.orion.visor.module.infra.entity.request.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 数据分组关联 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupRelCreateRequest", description = "数据分组关联 创建请求对象")
public class DataGroupRelCreateRequest implements Serializable {

    @NotNull
    @Schema(description = "组id")
    private Long groupId;

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

}
