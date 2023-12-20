package com.orion.ops.framework.common.handler.data.model;

import com.alibaba.fastjson.JSON;

/**
 * 标准数据模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 22:07
 */
public interface GenericsDataModel {

    /**
     * 序列化
     *
     * @return json
     */
    default String serial() {
        return JSON.toJSONString(this);
    }

}
