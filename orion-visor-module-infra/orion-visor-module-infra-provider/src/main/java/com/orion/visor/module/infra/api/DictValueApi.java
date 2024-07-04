package com.orion.visor.module.infra.api;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 字典配置值 对外服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/4 18:54
 */
public interface DictValueApi {

    /**
     * 查询字典配置值
     *
     * @param key key
     * @return rows
     */
    List<JSONObject> getDictValue(String key);

    /**
     * 查询字典配置值
     *
     * @param keys keys
     * @return rows
     */
    Map<String, List<JSONObject>> getDictValueList(List<String> keys);

}
