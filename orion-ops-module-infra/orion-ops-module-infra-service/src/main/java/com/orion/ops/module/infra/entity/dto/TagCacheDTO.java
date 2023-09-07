package com.orion.ops.module.infra.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/5 15:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(name = "TagCacheDTO", description = "菜单 缓存业务对象")
public class TagCacheDTO {

    @EqualsAndHashCode.Include
    @Schema(description = "id")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

}
