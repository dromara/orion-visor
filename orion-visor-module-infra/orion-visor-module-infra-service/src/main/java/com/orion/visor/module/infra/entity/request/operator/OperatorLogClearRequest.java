package com.orion.visor.module.infra.entity.request.operator;

import com.orion.visor.framework.common.entity.DataClearRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 操作日志 清理请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "OperatorLogClearRequest", description = "操作日志 清理请求对象")
public class OperatorLogClearRequest extends OperatorLogQueryRequest implements DataClearRequest {

    @NotNull
    @Min(value = 1)
    @Max(value = 2000)
    @Schema(description = "清理数量限制")
    private Integer limit;

}
