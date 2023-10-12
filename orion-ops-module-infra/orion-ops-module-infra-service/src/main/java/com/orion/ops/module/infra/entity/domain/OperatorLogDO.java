package com.orion.ops.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

/**
 * 操作日志 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "operator_log", autoResultMap = true)
@Schema(name = "OperatorLogDO", description = "操作日志 实体对象")
public class OperatorLogDO extends BaseDO {

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

    @Schema(description = "traceId")
    @TableField("trace_id")
    private String traceId;

    @Schema(description = "请求ip")
    @TableField("address")
    private String address;

    @Schema(description = "请求地址")
    @TableField("location")
    private String location;

    @Schema(description = "userAgent")
    @TableField("user_agent")
    private String userAgent;

    @Schema(description = "模块")
    @TableField("module")
    private String module;

    @Schema(description = "操作类型")
    @TableField("type")
    private String type;

    @Schema(description = "日志")
    @TableField("log_info")
    private String logInfo;

    @Schema(description = "参数")
    @TableField("extra")
    private String extra;

    @Schema(description = "操作结果 0失败 1成功")
    @TableField("result")
    private Integer result;

    @Schema(description = "错误信息")
    @TableField("error_message")
    private String errorMessage;

    @Schema(description = "返回值")
    @TableField("return_value")
    private String returnValue;

    @Schema(description = "操作时间")
    @TableField("duration")
    private Integer duration;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;

    @Schema(description = "修改时间")
    @TableField(exist = false)
    private Date updateTime;

    @Schema(description = "创建人")
    @TableField(exist = false)
    private String creator;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

    @Schema(description = "是否删除 0未删除 1已删除")
    @TableField(exist = false)
    private Boolean deleted;

}
