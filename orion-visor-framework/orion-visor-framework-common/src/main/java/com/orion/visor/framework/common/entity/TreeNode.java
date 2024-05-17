package com.orion.visor.framework.common.entity;

import java.util.List;

/**
 * 树节点
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 16:52
 */
public interface TreeNode<T extends TreeNode<T>> {

    /**
     * id
     *
     * @return id
     */
    Long getId();

    /**
     * parentId
     *
     * @return parentId
     */
    Long getParentId();

    /**
     * sort
     *
     * @return sort
     */
    Integer getSort();

    /**
     * children
     *
     * @return children
     */
    List<T> getChildren();

    /**
     * 设置 children
     *
     * @param children children
     */
    void setChildren(List<T> children);

}
