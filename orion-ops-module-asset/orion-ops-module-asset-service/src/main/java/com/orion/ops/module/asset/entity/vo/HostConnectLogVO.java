package com.orion.ops.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 主机连接日志 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostConnectLogVO", description = "主机连接日志 视图响应对象")
public class HostConnectLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "主机名称")
    private String hostName;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "额外信息")
    private String extraInfo;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

}
