package com.orion.visor.module.infra.entity.request.tag;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 标签枚举 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TagQueryRequest", description = "标签枚举 查询请求对象")
public class TagQueryRequest extends PageRequest {

    @Size(max = 12)
    @Schema(description = "标签类型")
    private String type;

}
