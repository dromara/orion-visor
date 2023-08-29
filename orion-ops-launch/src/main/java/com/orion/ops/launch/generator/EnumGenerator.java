package com.orion.ops.launch.generator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.orion.lang.define.collect.MultiLinkedHashMap;
import com.orion.lang.utils.Enums;
import com.orion.lang.utils.ansi.AnsiAppender;
import com.orion.lang.utils.ansi.style.AnsiFont;
import com.orion.lang.utils.ansi.style.color.AnsiForeground;
import com.orion.lang.utils.awt.Clipboards;
import com.orion.lang.utils.reflect.Fields;
import com.orion.ops.module.infra.enums.UserStatusEnum;

import java.util.List;
import java.util.function.Function;

/**
 * 前端枚举生成器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 14:55
 */
public class EnumGenerator {

    public static void main(String[] args) {
        // 生成前端枚举配置
        String gen = gen(UserStatusEnum.class,
                UserStatusEnum::getStatus,
                UserStatusEnum::name);
        System.out.println(gen);
    }

    public static <E extends Enum<?>> String gen(Class<E> clazz,
                                                 Function<E, Object> valueFunction) {
        return gen(clazz, valueFunction, Enum::name);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<?>> String gen(Class<E> clazz,
                                                 Function<E, Object> valueFunction,
                                                 Function<E, Object> labelFunction) {
        // 获取枚举
        Enum<?>[] constants = clazz.getEnumConstants();
        // 获取字段
        List<String> fields = Enums.getFields(clazz);
        MultiLinkedHashMap<String, String, Object> result = MultiLinkedHashMap.create();
        for (Enum<?> e : constants) {
            String name = e.name();
            result.put(name, "value", valueFunction.apply((E) e));
            result.put(name, "label", labelFunction.apply((E) e));
            for (String field : fields) {
                result.put(name, field, Fields.getFieldValue(e, field));
            }
        }
        // ts 代码
        String tsCode = "/**\n *\n */\nexport const " + clazz.getSimpleName() + " = " + JSON.toJSONString(result, SerializerFeature.PrettyFormat);
        // 复制到剪切板
        Clipboards.setString(tsCode);
        // 提示
        String tips = AnsiAppender.create()
                .append(AnsiForeground.BRIGHT_BLUE.and(AnsiFont.BOLD), "代码生成完成 - 已复制到剪切板 ^_^")
                .toString();
        return "\n" + tsCode + "\n\n" + tips;
    }

}
