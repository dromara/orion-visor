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
 * 执行日志 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exec_log", autoResultMap = true)
@Schema(name = "ExecLogDO", description = "执行日志 实体对象")
public class ExecLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "执行用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "执行来源")
    @TableField("source")
    private String source;

    @Schema(description = "执行来源id")
    @TableField("source_id")
    private Long sourceId;

    @Schema(description = "执行描述")
    @TableField("desc")
    private String desc;

    @Schema(description = "执行命令")
    @TableField("command")
    private String command;

    @Schema(description = "执行状态")
    @TableField("status")
    private String status;

    @Schema(description = "执行开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "执行完成时间")
    @TableField("finish_time")
    private Date finishTime;

}
