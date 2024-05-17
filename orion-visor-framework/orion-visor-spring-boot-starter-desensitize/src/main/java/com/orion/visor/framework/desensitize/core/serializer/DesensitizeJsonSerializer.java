package com.orion.visor.framework.desensitize.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Desensitizes;
import com.orion.lang.utils.Objects1;
import com.orion.visor.framework.desensitize.core.annotation.Desensitize;

import java.io.IOException;
import java.util.Objects;

/**
 * jackson 脱敏序列化器
 * <p>
 * 用于 http 响应
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/11 17:10
 */
public class DesensitizeJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    /**
     * 这里是线程安全的
     * 同一个字段使用同一个 serializer 所以不需要使用 TheadLocal
     */
    private Desensitize desensitize;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Desensitize desensitize = property.getAnnotation(Desensitize.class);
        if (Objects.nonNull(desensitize)) {
            this.desensitize = desensitize;
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 为空判断
        if (value == null) {
            gen.writeNull();
            return;
        }
        if (desensitize.toEmpty()) {
            // 设置为空
            gen.writeString(Const.EMPTY);
        } else {
            // 脱敏
            String mix = Desensitizes.mix(Objects1.toString(value), desensitize.keepStart(), desensitize.keepEnd(), desensitize.replacer());
            gen.writeString(mix);
        }
    }

}