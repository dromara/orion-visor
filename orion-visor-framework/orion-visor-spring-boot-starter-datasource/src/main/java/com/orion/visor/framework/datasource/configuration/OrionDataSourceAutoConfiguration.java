package com.orion.visor.framework.datasource.configuration;

import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.orion.visor.framework.common.constant.AutoConfigureOrderConst;
import com.orion.visor.framework.datasource.core.filter.DruidAdRemoveFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据源配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/23 17:22
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_DATASOURCE)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(DruidStatProperties.class)
public class OrionDataSourceAutoConfiguration {

    /**
     * @param properties 配置
     * @return druid 广告过滤器
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.web-stat-filter.enabled", havingValue = "true")
    public FilterRegistrationBean<DruidAdRemoveFilter> druidAdRemoveFilterFilter(DruidStatProperties properties) {
        // 获取 druid web 监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取 common.js 的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        // 创建 DruidAdRemoveFilter Bean
        FilterRegistrationBean<DruidAdRemoveFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new DruidAdRemoveFilter());
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }

}
