package com.orion.ops.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 主机分组引用 更新请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostGroupRelUpdateRequest", description = "主机分组引用 更新请求对象")
public class HostGroupRelUpdateRequest implements Serializable {

    @NotNull
    @Schema(description = "分组id")
    private Long groupId;

    @Schema(description = "主机id")
    private List<Long> hostIdList;

}
