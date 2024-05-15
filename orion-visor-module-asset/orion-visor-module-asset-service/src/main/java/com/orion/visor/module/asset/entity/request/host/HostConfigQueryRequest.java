package com.orion.visor.module.asset.entity.request.host;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

/**
 * 主机配置 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-13 14:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostConfigQueryRequest", description = "主机配置 查询请求对象")
public class HostConfigQueryRequest extends PageRequest {

    @Schema(description = "主机id")
    private Long hostId;

    @Size(max = 32)
    @Schema(description = "配置类型")
    private String type;

}
