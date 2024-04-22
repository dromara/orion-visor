package com.orion.ops.module.asset.entity.request.exec;

import com.orion.ops.framework.desensitize.core.annotation.Desensitize;
import com.orion.ops.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 执行模板 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "ExecTemplateCreateRequest", description = "执行模板 创建请求对象")
public class ExecTemplateCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "名称")
    private String name;

    @NotBlank
    @Desensitize(toEmpty = true)
    @Schema(description = "命令")
    private String command;

    @NotNull
    @Schema(description = "超时时间秒 0不超时")
    private Integer timeout;

    @NotNull
    @Schema(description = "是否使用脚本执行")
    private Integer scriptExec;

    @Schema(description = "参数定义")
    private String parameterSchema;

    @Schema(description = "模板主机")
    private List<Long> hostIdList;

}
