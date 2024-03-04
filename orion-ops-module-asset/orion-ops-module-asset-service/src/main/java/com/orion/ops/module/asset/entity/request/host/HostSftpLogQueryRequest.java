package com.orion.ops.module.asset.entity.request.host;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.ops.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * SFTP 操作日志 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-3-4 22:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostSftpLogQueryRequest", description = "SFTP 操作日志 查询请求对象")
public class HostSftpLogQueryRequest extends PageRequest {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "hostId")
    private Long hostId;

    @Size(max = 64)
    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "操作结果 0失败 1成功")
    private Integer result;

    @Schema(description = "开始时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] startTimeRange;

}
