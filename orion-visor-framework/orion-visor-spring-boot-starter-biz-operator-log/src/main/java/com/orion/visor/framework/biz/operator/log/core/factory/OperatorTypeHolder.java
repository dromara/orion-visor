package com.orion.visor.framework.biz.operator.log.core.factory;

import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志类型实例
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 14:43
 */
public class OperatorTypeHolder {

    private static final Map<String, OperatorType> TYPES = new HashMap<>();

    private OperatorTypeHolder() {
    }

    /**
     * 获取类型
     *
     * @param key key
     * @return type
     */
    public static OperatorType get(String key) {
        return TYPES.get(key);
    }

    /**
     * 设置类型
     *
     * @param type type
     */
    public static void set(OperatorType type) {
        TYPES.put(type.getType(), type);
    }

}
