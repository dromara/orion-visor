package com.orion.ops.module.infra.handler.preference.model;

import com.alibaba.fastjson.JSON;

import java.util.Map;

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
    default Map<String, Object> toMap() {
        return JSON.parseObject(JSON.toJSONString(this));
    }

}
