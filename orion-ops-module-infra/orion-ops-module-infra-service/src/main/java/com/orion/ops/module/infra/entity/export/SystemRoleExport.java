package com.orion.ops.module.infra.entity.export;

import com.orion.lang.utils.time.Dates;
import com.orion.office.excel.annotation.ExportField;
import com.orion.office.excel.annotation.ExportSheet;
import com.orion.office.excel.annotation.ExportTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 系统角色导出
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/31 17:40
 */
@Data
@ExportTitle(title = SystemRoleExport.TITLE)
@ExportSheet(name = "系统角色", filterHeader = true, freezeHeader = true, indexToSort = true)
public class SystemRoleExport {

    public static final String TITLE = "系统角色导出";

    @ExportField(index = 0, header = "id", width = 16)
    @Schema(description = "id")
    private Long id;

    @ExportField(index = 1, header = "角色名称", width = 16)
    @Schema(description = "角色名称")
    private String name;

    @ExportField(index = 2, header = "角色编码", width = 16)
    @Schema(description = "角色编码")
    private String code;

    @ExportField(index = 3, header = "状态", width = 16)
    @Schema(description = "状态 0停用 1启用")
    private Integer status;

    @ExportField(index = 4, header = "创建时间", width = 16, format = Dates.YMD_HMS)
    @Schema(description = "创建时间")
    private Date createTime;

    @ExportField(index = 5, header = "修改时间", width = 16, format = Dates.YMD_HMS)
    @Schema(description = "修改时间")
    private Date updateTime;

    @ExportField(index = 6, header = "创建人", width = 16)
    @Schema(description = "创建人")
    private String creator;

    @ExportField(index = 7, header = "修改人", width = 16)
    @Schema(description = "修改人")
    private String updater;

}
