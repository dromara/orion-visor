package com.orion.ops.module.infra.handler.preference.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.lang.utils.Refs;
import com.orion.lang.utils.collect.Maps;

import java.util.Map;
import java.util.function.Function;

/**
 * 偏好
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/8 13:54
 */
public interface PreferenceModel {

    /**
     * 转为 map
     *
     * @return map
     */
    default Map<String, String> toMap() {
        JSONObject map = JSON.parseObject(JSON.toJSONString(this));
        return Maps.map(map, Function.identity(), Refs::json);
    }

}
