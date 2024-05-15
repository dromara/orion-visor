package com.orion.visor.module.infra.entity.dto;

import com.orion.lang.define.cache.key.model.LongCacheIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

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
public class TagCacheDTO implements LongCacheIdModel, Serializable {

    @EqualsAndHashCode.Include
    @Schema(description = "id")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

}
