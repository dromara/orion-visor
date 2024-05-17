package com.orion.visor.module.infra.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统消息数量对象
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024/5/11 18:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemMessageCountDTO", description = "系统消息数量对象")
public class SystemMessageCountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "消息分类")
    private String classify;

    @Schema(description = "数量")
    private Integer count;

}