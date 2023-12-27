package com.orion.ops.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * 主机连接日志 实体对象
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
@TableName(value = "host_connect_log", autoResultMap = true)
@Schema(name = "HostConnectLogDO", description = "主机连接日志 实体对象")
public class HostConnectLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "主机id")
    @TableField("host_id")
    private Long hostId;

    @Schema(description = "主机名称")
    @TableField("host_name")
    private String hostName;

    @Schema(description = "主机地址")
    @TableField("host_address")
    private String hostAddress;

    @Schema(description = "类型")
    @TableField("type")
    private String type;

    @Schema(description = "token")
    @TableField("token")
    private String token;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;

    @Schema(description = "额外信息")
    @TableField("extra_info")
    private String extraInfo;

    @Schema(description = "创建人")
    @TableField(exist = false)
    private String creator;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

}
