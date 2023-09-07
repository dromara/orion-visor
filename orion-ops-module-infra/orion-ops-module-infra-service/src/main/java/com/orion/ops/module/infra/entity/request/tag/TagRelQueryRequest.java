package com.orion.ops.module.infra.entity.request.tag;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * 标签引用 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TagRelQueryRequest", description = "标签引用 查询请求对象")
public class TagRelQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "标签id")
    private Long tagId;

    @Size(max = 32)
    @Schema(description = "标签名称")
    private String tagName;

    @Size(max = 12)
    @Schema(description = "标签类型")
    private String tagType;

    @Schema(description = "关联id")
    private Long relId;

    @Schema(description = "关联id")
    private Collection<Long> relIdList;

}
