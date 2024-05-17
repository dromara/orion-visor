package com.orion.visor.framework.common.utils;

import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.reflect.Annotations;
import com.orion.visor.framework.common.constant.Const;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * swagger 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 14:52
 */
public class SwaggerUtils {

    /**
     * api 描述
     */
    private static final Map<String, String> SUMMARY_MAPPING = Maps.newMap();

    private SwaggerUtils() {
    }

    /**
     * 获取 api 描述
     *
     * @param m method
     * @return summary
     */
    public static String getOperationSummary(Method m) {
        // 缓存中获取描述
        String key = m.toString();
        String cache = SUMMARY_MAPPING.get(key);
        if (cache != null) {
            return cache;
        }
        // 获取注解描述
        Operation operation = Annotations.getAnnotation(m, Operation.class);
        String summary = Const.EMPTY;
        if (operation != null) {
            summary = operation.summary();
        }
        SUMMARY_MAPPING.put(key, summary);
        return summary;
    }

}
