package com.orion.ops.framework.common.handler.data.strategy;

import com.orion.ops.framework.common.handler.data.model.GenericsDataModel;

import java.util.Map;

/**
 * map 数据处理策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:11
 */
public interface MapDataStrategy<Config extends GenericsDataModel> extends GenericsDataStrategy<Config, Map<String, Object>> {

}
