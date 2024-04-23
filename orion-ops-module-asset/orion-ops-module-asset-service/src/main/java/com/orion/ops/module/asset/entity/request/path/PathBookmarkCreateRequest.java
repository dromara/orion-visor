package com.orion.ops.module.asset.entity.request.path;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 路径标签 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkCreateRequest", description = "路径标签 创建请求对象")
public class PathBookmarkCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "分组id")
    private Long groupId;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @NotBlank
    @Size(max = 1024)
    @Schema(description = "路径")
    private String path;

}
