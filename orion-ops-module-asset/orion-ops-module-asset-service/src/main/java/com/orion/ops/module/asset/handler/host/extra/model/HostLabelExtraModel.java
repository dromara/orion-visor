package com.orion.ops.module.asset.handler.host.extra.model;

import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机拓展信息 - 标签模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/29 23:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostLabelExtraModel", description = "主机拓展信息 - 标签模型")
public class HostLabelExtraModel implements GenericsDataModel {

    @Schema(description = "别名")
    private String alias;

    @Schema(description = "颜色")
    private String color;

}
