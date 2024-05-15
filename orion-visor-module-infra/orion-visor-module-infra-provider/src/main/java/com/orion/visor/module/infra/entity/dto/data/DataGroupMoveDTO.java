package com.orion.visor.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 数据分组 移动请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataGroupMoveDTO", description = "数据分组 移动请求业务对象")
public class DataGroupMoveDTO implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "目标id")
    private Long targetId;

    @NotNull
    @Schema(description = "移动位置")
    private Integer position;

}
