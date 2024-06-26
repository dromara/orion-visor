package com.orion.visor.module.infra.entity.request.dict;

import com.orion.visor.framework.common.constant.ValidConst;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 字典配置项 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DictKeyUpdateRequest", description = "字典配置项 更新请求对象")
public class DictKeyUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 32)
    @Pattern(regexp = ValidConst.DICT_1_32_PATTERN, message = ValidConst.DICT_1_32_MESSAGE)
    @Schema(description = "配置项")
    private String keyName;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "配置值定义")
    private String valueType;

    @Schema(description = "额外配置定义")
    private String extraSchema;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "配置描述")
    private String description;

}
