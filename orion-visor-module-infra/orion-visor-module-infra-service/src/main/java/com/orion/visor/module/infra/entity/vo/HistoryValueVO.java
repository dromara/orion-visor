package com.orion.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典配置项 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HistoryValueVO", description = "字典配置项 视图响应对象")
public class HistoryValueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "修改前")
    private String beforeValue;

    @Schema(description = "修改后")
    private String afterValue;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    private String creator;

}
