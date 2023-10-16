package com.orion.ops.module.infra.entity.dto.history;

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
 * 历史归档 创建请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HistoryValueCreateDTO", description = "历史归档 创建请求业务对象")
public class HistoryValueCreateDTO implements Serializable {

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

    @NotBlank
    @Schema(description = "修改前")
    private String beforeValue;

    @NotBlank
    @Schema(description = "修改后")
    private String afterValue;

}
