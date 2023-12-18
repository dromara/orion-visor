package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 主机别名 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-13 14:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostAliasUpdateRequest", description = "主机别名 更新请求对象")
public class HostAliasUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @Size(max = 32)
    @Schema(description = "别名")
    private String name;

}
