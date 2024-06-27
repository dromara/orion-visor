package com.orion.visor.module.asset.define.config;

import com.orion.visor.framework.common.entity.AutoClearConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 批量执行日志自动清理配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/24 15:01
 */
@Data
@Component
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "app.auto-clear.exec-log")
public class AppExecLogAutoClearConfig extends AutoClearConfig {

}
