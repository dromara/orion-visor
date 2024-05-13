package com.orion.ops.module.asset.entity.request.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 上传任务 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "UploadTaskQueryRequest", description = "上传任务 查询请求对象")
public class UploadTaskQueryRequest extends PageRequest {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Size(max = 128)
    @Schema(description = "描述")
    private String description;

    @Schema(description = "远程路径")
    private String remotePath;

    @Size(max = 16)
    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] createTimeRange;

}
