package com.orion.ops.module.asset.entity.request.exec;

import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量执行 请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecRequest", description = "批量执行 请求对象")
public class ExecRequest extends PageRequest {

    @Schema(description = "执行模板id")
    private Long templateId;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "执行描述")
    private String desc;

    @NotBlank
    @Schema(description = "执行命令")
    private String command;

    @NotBlank
    @Schema(description = "执行参数")
    private String parameter;

    @NotEmpty
    @Schema(description = "执行主机")
    private List<Long> hostIdList;

}
