package com.orion.ops.module.infra.entity.request.history;

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
 * 历史归档 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HistoryValueCreateRequest", description = "历史归档 创建请求对象")
public class HistoryValueCreateRequest implements Serializable {

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

    @NotBlank
    @Size(max = 16)
    @Schema(description = "类型")
    private String type;

    @NotBlank
    @Schema(description = "修改前")
    private String beforeValue;

    @NotBlank
    @Schema(description = "修改后")
    private String afterValue;

}
