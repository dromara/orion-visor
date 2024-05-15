package com.orion.ops.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMessageVO", description = "系统消息 视图响应对象")
public class SystemMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "消息分类")
    private String classify;

    @Schema(description = "消息类型")
    private String type;

    @Schema(description = "消息状态")
    private Integer status;

    @Schema(description = "消息关联")
    private String relKey;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;

}
