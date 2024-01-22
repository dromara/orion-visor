package com.orion.ops.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.orion.ops.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;
import java.math.*;

/**
 * 命令片段 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "command_snippet", autoResultMap = true)
@Schema(name = "CommandSnippetDO", description = "命令片段 实体对象")
public class CommandSnippetDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "触发前缀")
    @TableField("prefix")
    private String prefix;

    @Schema(description = "代码片段")
    @TableField("command")
    private String command;

}
