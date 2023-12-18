package com.orion.ops.module.infra.entity.request.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 数据别名 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataAliasUpdateRequest", description = "数据别名 更新请求对象")
public class DataAliasUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "用户id")
    private Long userId;

    @NotNull
    @Schema(description = "数据id")
    private Long relId;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "数据类型")
    private String type;

    @Size(max = 32)
    @Schema(description = "别名")
    private String alias;

}
