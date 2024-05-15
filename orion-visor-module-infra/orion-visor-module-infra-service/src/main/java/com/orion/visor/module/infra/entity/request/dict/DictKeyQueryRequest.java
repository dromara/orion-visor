package com.orion.visor.module.infra.entity.request.dict;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 字典配置项 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-19 10:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DictKeyQueryRequest", description = "字典配置项 查询请求对象")
public class DictKeyQueryRequest extends PageRequest {

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "配置项")
    private String keyName;

    @Size(max = 64)
    @Schema(description = "配置描述")
    private String description;

}
