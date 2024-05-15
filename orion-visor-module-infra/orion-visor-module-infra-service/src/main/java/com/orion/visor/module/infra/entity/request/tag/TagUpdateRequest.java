package com.orion.visor.module.infra.entity.request.tag;

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
 * 标签枚举 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TagUpdateRequest", description = "标签枚举 更新请求对象")
public class TagUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "标签名称")
    private String name;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "标签类型")
    private String type;

}
