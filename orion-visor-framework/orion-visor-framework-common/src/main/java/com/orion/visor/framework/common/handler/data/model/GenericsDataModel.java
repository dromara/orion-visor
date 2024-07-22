package com.orion.visor.framework.common.handler.data.model;

import com.alibaba.fastjson.JSON;

import java.util.Map;

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


    /**
     * 转为 map
     *
     * @return map
     */
    default Map<String, Object> toMap() {
        return JSON.parseObject(this.serial());
    }

}
