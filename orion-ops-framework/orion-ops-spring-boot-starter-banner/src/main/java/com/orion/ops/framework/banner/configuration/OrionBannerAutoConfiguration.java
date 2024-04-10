package com.orion.ops.framework.banner.configuration;

import com.orion.ops.framework.banner.core.runner.BannerApplicationRunner;
import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;

/**
 * banner 自动配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/15 16:16
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_BANNER)
public class OrionBannerAutoConfiguration {

    /**
     * @return banner 打印器
     */
    @Bean
    public BannerApplicationRunner bannerApplicationRunner() {
        return new BannerApplicationRunner();
    }

}
