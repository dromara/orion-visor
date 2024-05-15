package com.orion.visor.module.infra.entity.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 标签引用 业务对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TagDTO", description = "标签 业务对象")
public class TagDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

}
