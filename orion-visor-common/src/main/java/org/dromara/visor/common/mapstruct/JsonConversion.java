/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.common.mapstruct;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json 转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/7 17:43
 */
public class JsonConversion {

    private JsonConversion() {
    }

    /**
     * JSONString > JSONObject
     *
     * @param json json
     * @return JSONObject
     */
    public static JSONObject stringToJsonObject(String json) {
        return json != null ? JSON.parseObject(json) : null;
    }

    /**
     * JSONString > JSONArray.
     *
     * @param json json
     * @return JSONArray
     */
    public static JSONArray stringToJsonArray(String json) {
        return json != null ? JSON.parseArray(json) : null;
    }

    /**
     * JSONObject > JSONString.
     *
     * @param jsonObject JSONObject
     * @return JSONString
     */
    public static String jsonObjectToString(JSONObject jsonObject) {
        return jsonObject != null ? jsonObject.toJSONString() : null;
    }

    /**
     * JSONArray > JSONString.
     *
     * @param jsonArray JSONArray
     * @return JSONString
     */
    public static String jsonArrayToString(JSONArray jsonArray) {
        return jsonArray != null ? jsonArray.toJSONString() : null;
    }

}
