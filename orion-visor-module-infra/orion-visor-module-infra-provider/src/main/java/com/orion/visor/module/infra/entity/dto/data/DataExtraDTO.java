package com.orion.visor.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据拓展信息 业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 22:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataExtraDTO", description = "数据拓展信息 业务对象")
public class DataExtraDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "数据id")
    private Long relId;

    @Schema(description = "数据类型")
    private String type;

    @Schema(description = "配置项")
    private String item;

    @Schema(description = "配置值")
    private String value;

}
