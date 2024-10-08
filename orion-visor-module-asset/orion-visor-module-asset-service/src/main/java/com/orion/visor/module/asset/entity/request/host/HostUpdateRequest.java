package com.orion.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * 主机 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostUpdateRequest", description = "主机 更新请求对象")
public class HostUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "id")
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "主机名称")
    private String name;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "主机编码")
    private String code;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "主机地址")
    private String address;

    @NotNull
    @Min(value = 1)
    @Max(value = 65535)
    @Schema(description = "主机端口")
    private Integer port;

    @Schema(description = "主机分组")
    private List<Long> groupIdList;

    @Size(max = 5)
    @Schema(description = "tags")
    private List<Long> tags;

}
