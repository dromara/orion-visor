package com.orion.ops.module.infra.entity.request.dict;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 字典配置值 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DictValueQueryRequest", description = "字典配置值 查询请求对象")
public class DictValueQueryRequest extends PageRequest {

    @Schema(description = "配置项id")
    private Long keyId;

    @Size(max = 32)
    @Schema(description = "配置项名称")
    private String keyName;

    @Size(max = 512)
    @Schema(description = "配置值")
    private String value;

    @Size(max = 64)
    @Schema(description = "配置描述")
    private String label;

    @Schema(description = "额外参数")
    private String extra;

}
