package com.orion.ops.module.infra.entity.request.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 数据权限 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataPermissionUpdateRequest", description = "数据权限 更新请求对象")
public class DataPermissionUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "引用id")
    private List<Long> relIdList;

    @Schema(description = "数据类型")
    private String type;

}
