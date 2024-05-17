package com.orion.visor.framework.mybatis.core.domain;

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

    private static final long serialVersionUID = 1L;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String creator;

    @Schema(description = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updater;

    /**
     * @see com.orion.visor.framework.common.constant.Const#NOT_DELETE
     * @see com.orion.visor.framework.common.constant.Const#IS_DELETED
     */
    @Schema(description = "是否删除 0未删除 1已删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.TINYINT)
    private Boolean deleted;

}
