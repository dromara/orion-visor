package com.orion.ops.module.infra.entity.dto.history;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史归档 业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HistoryValueDTO", description = "历史归档 业务对象")
public class HistoryValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "引用id")
    private Long relId;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "修改前")
    private String beforeValue;

    @Schema(description = "修改后")
    private String afterValue;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "修改人")
    private String updater;

}
