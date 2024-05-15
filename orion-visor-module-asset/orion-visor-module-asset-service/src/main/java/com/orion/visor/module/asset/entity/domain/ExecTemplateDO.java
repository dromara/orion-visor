package com.orion.visor.module.asset.entity.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.orion.visor.framework.mybatis.core.domain.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 执行模板 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exec_template", autoResultMap = true)
@Schema(name = "ExecTemplateDO", description = "执行模板 实体对象")
public class ExecTemplateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "命令")
    @TableField("command")
    private String command;

    @Schema(description = "超时时间秒 0不超时")
    @TableField("timeout")
    private Integer timeout;

    @Schema(description = "是否使用脚本执行")
    @TableField("script_exec")
    private Integer scriptExec;

    @Schema(description = "参数定义")
    @TableField("parameter_schema")
    private String parameterSchema;

}
