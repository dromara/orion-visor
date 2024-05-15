package com.orion.visor.module.infra.entity.request.favorite;

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
 * 收藏 操作请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FavoriteOperatorRequest", description = "收藏 操作请求对象")
public class FavoriteOperatorRequest implements Serializable {

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "收藏类型")
    private String type;

}
