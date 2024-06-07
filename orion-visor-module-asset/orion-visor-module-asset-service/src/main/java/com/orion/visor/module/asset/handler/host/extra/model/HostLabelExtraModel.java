package com.orion.visor.module.asset.handler.host.extra.model;

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
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
public class HostLabelExtraModel implements GenericsDataModel {

    /**
     * 别名
     */
    private String alias;

    /**
     * 颜色
     */
    private String color;

}
