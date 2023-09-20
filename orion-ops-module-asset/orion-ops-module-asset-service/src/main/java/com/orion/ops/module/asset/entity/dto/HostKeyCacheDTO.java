package com.orion.ops.module.asset.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 主机秘钥缓存
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 13:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostKeyCacheDTO", description = "主机秘钥缓存")
public class HostKeyCacheDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "名称")
    private String name;

}
