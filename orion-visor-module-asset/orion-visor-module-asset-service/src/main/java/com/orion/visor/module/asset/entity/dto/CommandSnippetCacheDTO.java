package com.orion.visor.module.asset.entity.dto;

import com.orion.lang.define.cache.key.model.LongCacheIdModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 命令片段 缓存对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommandSnippetCacheDTO", description = "命令片段 缓存对象")
public class CommandSnippetCacheDTO implements LongCacheIdModel, Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "分组id")
    private Long groupId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "代码片段")
    private String command;

}
