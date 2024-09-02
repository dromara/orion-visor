package com.orion.visor.module.infra.entity.request.exoression;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * cron 下次执行时间请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/2 16:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CronNextRequest", description = "cron 下次执行时间请求对象")
public class CronNextRequest implements Serializable {

    @NotBlank
    @Schema(description = "cron 表达式")
    private String expression;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Schema(description = "次数")
    private Integer times;

}
