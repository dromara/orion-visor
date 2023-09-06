package com.orion.ops.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.*;
import java.math.*;

/**
 * 标签引用 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 17:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TagRelVO", description = "标签引用 视图响应对象")
public class TagRelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "标签类型")
    private String type;

    @Schema(description = "标签id")
    private Long tagId;

    @Schema(description = "关联id")
    private Long relId;

}
