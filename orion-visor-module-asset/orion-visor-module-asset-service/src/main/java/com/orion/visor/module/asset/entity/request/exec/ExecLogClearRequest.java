package com.orion.visor.module.asset.entity.request.exec;

import com.orion.visor.framework.common.entity.DataClearRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 批量执行日志 清理请求对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecLogClearRequest", description = "批量执行日志 清理请求对象")
public class ExecLogClearRequest extends ExecLogQueryRequest implements DataClearRequest {

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    @Schema(description = "清理数量限制")
    private Integer limit;

}
