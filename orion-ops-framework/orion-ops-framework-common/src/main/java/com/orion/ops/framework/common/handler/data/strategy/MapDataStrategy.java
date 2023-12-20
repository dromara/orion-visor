package com.orion.ops.framework.common.handler.data.strategy;

import com.alibaba.fastjson.JSONObject;
import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;

import java.util.Map;

/**
 * map 数据处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:11
 */
public interface MapDataStrategy<Model extends GenericsDataModel> extends GenericsDataStrategy<Model, Map<String, Object>> {

    @Override
    default Map<String, Object> toView(String model) {
        return JSONObject.parseObject(model);
    }

}
