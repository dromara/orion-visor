package com.orion.ops.framework.desensitize.core.processor;

import com.orion.lang.utils.reflect.Annotations;
import com.orion.lang.utils.reflect.Methods;
import com.orion.ops.framework.common.utils.Desensitizes;
import com.orion.ops.framework.common.annotation.Desensitize;
import com.orion.ops.framework.common.annotation.DoDesensitize;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 对象脱敏处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 17:21
 */
public class ObjectDesensitizeProcessor implements IDesensitizeProcessor<Object> {

    @Override
    public void execute(Object data) {
        if (data == null) {
            return;
        }
        Class<?> dataClass = data.getClass();
        // 获取被标注脱敏的字段
        Map<Field, Desensitize> annotatedFields = Annotations.getAnnotatedFields(dataClass, Desensitize.class, true);
        annotatedFields.forEach((f, d) -> {
            // 获取 getter 方法
            Method getter = Methods.getGetterMethodByCache(dataClass, f.getName());
            if (!getter.getReturnType().equals(String.class)) {
                // 非 string 不进行脱敏操作
                return;
            }
            // 调用 getter 方法
            String value = Methods.invokeMethod(data, getter);
            if (value == null) {
                // 为 null 不进行脱敏操作
                return;
            }
            // 数据脱敏
            String desensitizeValue = this.valueDesensitize(value, d);
            // 获取 setter 方法
            Method setter = Methods.getSetterMethodByCache(dataClass, f.getName());
            // 调用 setter 方法
            Methods.invokeMethod(data, setter, desensitizeValue);
        });
        // 获取被标注脱敏字段的对象
        Map<Field, DoDesensitize> annotatedObjects = Annotations.getAnnotatedFields(dataClass, DoDesensitize.class, true);
        annotatedObjects.forEach((f, d) -> {
            // 获取 getter 方法
            Method getter = Methods.getGetterMethodByCache(dataClass, f.getName());
            // 调用 getter 方法
            Object value = Methods.invokeMethod(data, getter);
            if (value == null) {
                // 为 null 不进行脱敏操作
                return;
            }
            // 执行对象脱敏操作
            this.execute(value);
        });
        return;
    }

    /**
     * 数据脱敏
     *
     * @param value value
     * @param c     配置
     * @return 脱敏字符
     */
    private String valueDesensitize(String value, Desensitize c) {
        return Desensitizes.mix(value, c.keepStart(), c.keepEnd(), c.replacer());
    }

}
