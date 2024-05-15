package com.orion.visor.module.infra.entity.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 数据拓展信息 查询请求业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DataExtraQueryDTO", description = "数据拓展信息 查询请求业务对象")
public class DataExtraQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "数据id")
    private Long relId;

    @Schema(description = "数据id")
    private List<Long> relIdList;

    @Size(max = 32)
    @Schema(description = "拓展项")
    private String item;

    @Schema(description = "拓展项")
    private List<String> items;

}
