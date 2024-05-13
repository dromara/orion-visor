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
 * 上传任务 实体对象
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
@TableName(value = "upload_task", autoResultMap = true)
@Schema(name = "UploadTaskDO", description = "上传任务 实体对象")
public class UploadTaskDO extends BaseDO {

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

    @Schema(description = "远程路径")
    @TableField("remote_path")
    private String remotePath;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "额外信息")
    @TableField("extra_info")
    private String extraInfo;

    @Schema(description = "文件数量")
    @TableField("file_count")
    private Integer fileCount;

    @Schema(description = "主机数量")
    @TableField("host_count")
    private Integer hostCount;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;

}
