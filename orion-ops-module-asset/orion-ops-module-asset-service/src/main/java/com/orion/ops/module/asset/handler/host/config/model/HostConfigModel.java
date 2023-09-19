package com.orion.ops.module.asset.handler.host.config.model;

import com.alibaba.fastjson.JSON;

/**
 * 主机配置父类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/13 14:47
 */
public interface HostConfigModel {

    /**
     * 序列化
     *
     * @return json
     */
    default String serial() {
        return JSON.toJSONString(this);
    }

}
