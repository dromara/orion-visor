package com.orion.ops.module.infra.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 系统消息 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "system_message", autoResultMap = true)
@Schema(name = "SystemMessageDO", description = "系统消息 实体对象")
public class SystemMessageDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "消息分类")
    @TableField("classify")
    private String classify;

    @Schema(description = "消息类型")
    @TableField("type")
    private String type;

    @Schema(description = "消息状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "消息关联")
    @TableField("rel_key")
    private String relKey;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "消息内容")
    @TableField("content")
    private String content;

    @Schema(description = "接收人id")
    @TableField("receiver_id")
    private Long receiverId;

    @Schema(description = "接收人用户名")
    @TableField("receiver_username")
    private String receiverUsername;

    @Schema(description = "创建人")
    @TableField(exist = false)
    private String creator;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

}
