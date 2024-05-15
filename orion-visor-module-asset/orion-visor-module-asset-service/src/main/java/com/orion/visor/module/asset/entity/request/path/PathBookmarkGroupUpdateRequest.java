package com.orion.visor.module.asset.entity.request.path;

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
 * 路径标签分组 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkGroupUpdateRequest", description = "路径标签分组 更新请求对象")
public class PathBookmarkGroupUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "分组名称")
    private String name;

}
