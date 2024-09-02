package com.orion.visor.framework.common.entity;

/**
 * 数据清理请求 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/29 11:26
 */
public interface DataClearRequest {

    /**
     * 获取清理数量限制
     *
     * @return 清理限制
     */
    Integer getLimit();

    /**
     * 设置清理数量限制
     *
     * @param limit limit
     */
    void setLimit(Integer limit);

}
