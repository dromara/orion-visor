package com.orion.visor.module.asset.entity.request.upload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.entity.PageRequest;
import com.orion.visor.framework.common.validator.group.Clear;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

    @NotNull(groups = Clear.class)
    @Min(value = 1, groups = Clear.class)
    @Max(value = 2000, groups = Clear.class)
    @Schema(description = "清理数量限制")
    private Integer clearLimit;

    public void setClearLimit(Integer clearLimit) {
        this.clearLimit = clearLimit;
        this.setPage(Const.N_1);
        this.setLimit(clearLimit);
    }

}
