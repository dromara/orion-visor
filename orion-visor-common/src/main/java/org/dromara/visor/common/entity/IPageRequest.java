package org.dromara.visor.common.entity;

/**
 * 页码请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/17 22:02
 */
public interface IPageRequest {

    /**
     * 页码
     *
     * @return page
     */
    Integer getPage();

    /**
     * 大小
     *
     * @return limit
     */
    Integer getLimit();

}
