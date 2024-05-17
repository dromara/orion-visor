package com.orion.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 主机拓展信息查询请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 21:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostExtraQueryRequest", description = "主机拓展信息查询请求")
public class HostExtraQueryRequest {

    @NotNull
    @Schema(description = "主机id")
    private Long hostId;

    @NotEmpty
    @Schema(description = "配置项")
    private List<String> items;

}
