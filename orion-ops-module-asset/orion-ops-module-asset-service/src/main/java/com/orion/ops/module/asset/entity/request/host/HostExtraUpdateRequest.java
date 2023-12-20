package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 主机拓展信息更新请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 21:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostExtraUpdateRequest", description = "主机拓展信息更新请求")
public class HostExtraUpdateRequest {

    @NotNull
    @Schema(description = "主机id")
    private Long hostId;

    @NotNull
    @Schema(description = "配置项")
    private String item;

    @NotBlank
    @Schema(description = "拓展信息")
    private String extra;

}
