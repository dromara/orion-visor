package org.dromara.visor.common.entity;

/**
 * 查询排序请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/17 22:04
 */
public interface IOrderRequest {

    /**
     * 查询排序
     *
     * @return sort 0DESC 1ASC 其他不排序
     */
    Integer getOrder();

}
