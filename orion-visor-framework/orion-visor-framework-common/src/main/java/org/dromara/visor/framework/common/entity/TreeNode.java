/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.common.entity;

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
