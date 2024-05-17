package com.orion.visor.module.infra.entity.dto;

import com.orion.lang.define.cache.key.model.LongCacheIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 字典配置项 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DictKeyCacheDTO", description = "字典配置项 缓存对象")
public class DictKeyCacheDTO implements LongCacheIdModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "配置项")
    private String keyName;

    @Schema(description = "配置值定义")
    private String valueType;

    @Schema(description = "额外配置定义")
    private String extraSchema;

    @Schema(description = "配置描述")
    private String description;

}
