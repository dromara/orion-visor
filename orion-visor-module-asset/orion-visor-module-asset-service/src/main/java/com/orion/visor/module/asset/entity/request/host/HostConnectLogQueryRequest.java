package com.orion.visor.module.asset.entity.request.host;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orion.visor.framework.common.entity.PageRequest;
import com.orion.visor.framework.common.validator.group.Id;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 主机连接日志 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostConnectLogQueryRequest", description = "主机连接日志 查询请求对象")
public class HostConnectLogQueryRequest extends PageRequest {

    @NotNull(groups = Id.class)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "id")
    private List<Long> idList;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "主机id")
    private Long hostId;

    @Size(max = 128)
    @Schema(description = "主机地址")
    private String hostAddress;

    @Size(max = 16)
    @Schema(description = "类型")
    private String type;

    @Size(max = 128)
    @Schema(description = "token")
    private String token;

    @Size(max = 16)
    @Schema(description = "状态")
    private String status;

    @Schema(description = "开始时间-区间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date[] startTimeRange;

    @Schema(description = "创建时间 <=")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeLe;

    @Schema(description = "状态")
    private List<String> statusList;

}
