package com.orion.visor.module.asset.entity.request.host;

import com.orion.visor.framework.common.entity.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 主机 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostQueryRequest", description = "主机 查询请求对象")
public class HostQueryRequest extends PageRequest {

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "id")
    private Long id;

    @Size(max = 64)
    @Schema(description = "主机名称")
    private String name;

    @Size(max = 64)
    @Schema(description = "主机编码")
    private String code;

    @Size(max = 128)
    @Schema(description = "主机地址")
    private String address;

    @Schema(description = "tag")
    private List<Long> tags;

    @Schema(description = "是否查询 tag 信息")
    private Boolean queryTag;

    @Schema(description = "是否查询配置信息")
    private Boolean queryConfig;

}
