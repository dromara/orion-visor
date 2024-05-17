package com.orion.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典配置值 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DictValueVO", description = "字典配置值 视图响应对象")
public class DictValueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "配置项id")
    private Long keyId;

    @Schema(description = "配置项")
    private String keyName;

    @Schema(description = "配置描述")
    private String keyDescription;

    @Schema(description = "配置值")
    private String value;

    @Schema(description = "配置描述")
    private String label;

    @Schema(description = "额外参数")
    private String extra;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "修改人")
    private String updater;

}
