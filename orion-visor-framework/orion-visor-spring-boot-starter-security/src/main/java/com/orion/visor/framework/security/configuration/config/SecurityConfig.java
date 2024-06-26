package com.orion.visor.framework.security.configuration.config;

import com.orion.visor.framework.common.utils.ConfigUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 安全配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 15:55
 */
@Data
@ConfigurationProperties("orion.security")
public class SecurityConfig {

    /**
     * 加密复杂度
     */
    private Integer passwordEncoderLength;

    /**
     * 匿名接口
     */
    private List<String> permitUrl;

    public SecurityConfig() {
        this.passwordEncoderLength = 4;
    }

    public void setPermitUrl(List<String> permitUrl) {
        this.permitUrl = ConfigUtils.parseStringList(permitUrl);
    }

}
