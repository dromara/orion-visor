package com.orion.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * cron 下次执行时间响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/2 16:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CronNextVO", description = "cron 下次执行时间响应对象")
public class CronNextVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表达式是否正确")
    private Boolean valid;

    @Schema(description = "下次执行时间")
    private List<String> next;

}
