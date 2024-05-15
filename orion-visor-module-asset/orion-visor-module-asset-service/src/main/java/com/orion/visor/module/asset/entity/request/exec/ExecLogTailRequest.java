package com.orion.visor.module.asset.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 执行日志查看 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecLogTailRequest", description = "执行日志查看 请求对象")
public class ExecLogTailRequest {

    @Schema(description = "执行来源")
    private String source;

    @NotNull
    @Schema(description = "执行id")
    private Long execId;

    @Schema(description = "执行主机id")
    private List<Long> hostExecIdList;

}
