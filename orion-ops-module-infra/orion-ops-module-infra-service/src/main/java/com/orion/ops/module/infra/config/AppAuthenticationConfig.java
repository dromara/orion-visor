package com.orion.ops.module.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用认证配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/5 18:26
 */
@Data
@Component
@ConfigurationProperties("app.authentication")
public class AppAuthenticationConfig {

    /**
     * 是否允许多端登录
     */
    private Boolean allowMultiDevice;

    /**
     * 是否允许凭证续签
     */
    private Boolean allowRefresh;

    /**
     * 凭证续签最大次数
     */
    private Integer maxRefreshCount;

    /**
     * 登录失败锁定次数
     */
    private Integer loginFailedLockCount;

    /**
     * 登录失败锁定时间 (分)
     */
    private Integer loginFailedLockTime;

}
