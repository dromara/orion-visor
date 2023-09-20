package com.orion.ops.module.asset.entity.export;

import com.orion.lang.utils.time.Dates;
import com.orion.office.excel.annotation.ExportField;
import com.orion.office.excel.annotation.ExportSheet;
import com.orion.office.excel.annotation.ExportTitle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.*;

/**
 * 主机身份 导出对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExportTitle(title = HostIdentityExport.TITLE)
@ExportSheet(name = "主机身份", filterHeader = true, freezeHeader = true, indexToSort = true)
@Schema(name = "HostIdentityExport", description = "主机身份导出对象")
public class HostIdentityExport implements Serializable {

    public static final String TITLE = "主机身份导出";

    @Schema(description = "id")
    @ExportField(index = 0, header = "id", width = 16)
    private Long id;

    @Schema(description = "名称")
    @ExportField(index = 1, header = "名称", width = 16)
    private String name;

    @Schema(description = "用户名")
    @ExportField(index = 2, header = "用户名", width = 16)
    private String username;

    @Schema(description = "用户密码")
    @ExportField(index = 3, header = "用户密码", width = 16)
    private String password;

    @Schema(description = "秘钥id")
    @ExportField(index = 4, header = "秘钥id", width = 16)
    private Long keyId;

    @ExportField(index = 5, header = "创建时间", width = 16, format = Dates.YMD_HMS)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    @ExportField(index = 6, header = "修改时间", width = 16, format = Dates.YMD_HMS)
    private Date updateTime;

    @Schema(description = "创建人")
    @ExportField(index = 7, header = "创建人", width = 16)
    private String creator;

    @Schema(description = "修改人")
    @ExportField(index = 8, header = "修改人", width = 16)
    private String updater;

}
