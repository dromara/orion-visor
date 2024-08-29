package com.orion.visor.module.asset.entity.request.upload;

import com.orion.visor.framework.common.entity.DataClearRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 上传任务 清理请求对象
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "UploadTaskClearRequest", description = "上传任务 清理请求对象")
public class UploadTaskClearRequest extends UploadTaskQueryRequest implements DataClearRequest {

    @NotNull
    @Min(value = 1)
    @Max(value = 2000)
    @Schema(description = "清理数量限制")
    private Integer limit;

}
