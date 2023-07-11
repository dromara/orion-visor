package com.orion.ops.framework.web.core.convert;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 序列化配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/11 14:57
 */
@Data
@ConfigurationProperties("orion.serializer")
public class SerializeConfig {

    /**
     * 不支持的序列化类型
     */
    private List<String> unsupportedClasses;

    public SerializeConfig() {
        this.unsupportedClasses = new ArrayList<>();
    }

}
