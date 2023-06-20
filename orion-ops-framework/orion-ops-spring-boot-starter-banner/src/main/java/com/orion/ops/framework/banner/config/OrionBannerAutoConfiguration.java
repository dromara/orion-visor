package com.orion.ops.framework.banner.config;

import com.orion.ops.framework.banner.core.BannerApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * banner 自动配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/15 16:16
 */
@AutoConfiguration
public class OrionBannerAutoConfiguration {

    /**
     * @return banner 打印器
     */
    @Bean
    public BannerApplicationRunner bannerApplicationRunner() {
        return new BannerApplicationRunner();
    }

}
