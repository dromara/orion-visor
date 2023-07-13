package com.orion.ops.framework.mybatis.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 18:42
 */
@Data
public class BaseDO implements Serializable {

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    @Schema(description = "创建人")
    private String creator;

    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    @Schema(description = "修改人")
    private String updater;

    /**
     * @see com.orion.ops.framework.common.constant.Const#NOT_DELETE
     * @see com.orion.ops.framework.common.constant.Const#IS_DELETED
     */
    @TableLogic
    @Schema(description = "是否删除 0未删除 1已删除")
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.BIT)
    private Boolean deleted;

}
