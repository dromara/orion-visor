package com.orion.ops.module.asset.entity.request.path;

import com.orion.ops.framework.common.validator.group.Id;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 路径标签分组 删除请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PathBookmarkGroupDeleteRequest", description = "路径标签分组 删除请求对象")
public class PathBookmarkGroupDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Id.class)
    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "是否删除组内数据")
    private Boolean deleteItem;

}
