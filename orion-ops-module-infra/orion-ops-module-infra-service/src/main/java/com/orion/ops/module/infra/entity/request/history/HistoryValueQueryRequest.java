package com.orion.ops.module.infra.entity.request.history;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 历史归档 查询请求对象
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
@Schema(name = "HistoryValueQueryRequest", description = "历史归档 查询请求对象")
public class HistoryValueQueryRequest extends PageRequest {

    @NotNull
    @Schema(description = "引用id")
    private Long relId;

    @Size(max = 16)
    @NotNull
    @Schema(description = "类型")
    private String type;

    @Schema(description = "搜索")
    private String searchValue;

}
