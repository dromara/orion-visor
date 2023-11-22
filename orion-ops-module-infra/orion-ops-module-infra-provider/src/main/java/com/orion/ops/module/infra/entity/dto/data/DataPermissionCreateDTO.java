package com.orion.ops.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;
import java.math.*;

/**
 * 数据权限 创建请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataPermissionCreateDTO", description = "数据权限 创建请求业务对象")
public class DataPermissionCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Schema(description = "用户id")
    private Long userId;

    @NotNull
    @Schema(description = "角色id")
    private Long roleId;

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "数据类型")
    private String type;

}
