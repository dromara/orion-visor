/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
