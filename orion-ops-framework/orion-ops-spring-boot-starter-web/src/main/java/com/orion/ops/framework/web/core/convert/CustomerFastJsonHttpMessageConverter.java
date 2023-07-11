package com.orion.ops.framework.web.core.convert;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.http.MediaType;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 自定义 fastjson 转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/11 11:46
 */
public class CustomerFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

    private final SerializeConfig serializeConfig;

    public CustomerFastJsonHttpMessageConverter(SerializeConfig serializeConfig) {
        this.serializeConfig = serializeConfig;
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        List<String> unsupportedClasses = serializeConfig.getUnsupportedClasses();
        if (unsupportedClasses != null) {
            if (unsupportedClasses.contains(contextClass.getName())) {
                return false;
            }
        }
        return super.canRead(type, contextClass, mediaType);
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return super.canWrite(type, clazz, mediaType);
    }

}
