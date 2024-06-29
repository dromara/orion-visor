package com.orion.visor.framework.common.entity;

import lombok.Data;

/**
 * 自动清理配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/24 15:03
 */
@Data
public class AutoClearConfig {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 保留周期 (天)
     */
    private Integer keepPeriod;

}
