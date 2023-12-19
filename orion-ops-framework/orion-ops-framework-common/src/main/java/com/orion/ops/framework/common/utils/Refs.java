package com.orion.ops.framework.common.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.define.wrapper.Ref;

/**
 * ref 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/19 18:07
 */
public class Refs {

    private Refs() {
    }

    /**
     * 转为 ref json
     *
     * @param o o
     * @return json
     */
    public static String toJson(Object o) {
        return JSON.toJSONString(Ref.of(o));
    }

    /**
     * ref json 转为 ref value
     *
     * @param json json
     * @return value
     */
    public static Object parseObject(String json) {
        Ref<?> ref = JSON.parseObject(json, Ref.class);
        if (ref == null) {
            return null;
        }
        return ref.getValue();
    }

}
