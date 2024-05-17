package com.orion.visor.module.infra.entity.request.favorite;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 收藏 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-1 10:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FavoriteQueryRequest", description = "收藏 查询请求对象")
public class FavoriteQueryRequest extends PageRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "引用id")
    private Long relId;

    @Schema(description = "收藏类型")
    private String type;

}
