/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.framework.common.utils;

import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.entity.TreeNode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 16:52
 */
public class TreeUtils {

    private TreeUtils() {
    }

    /**
     * 获取节点以及父节点
     *
     * @param nodes  nodes
     * @param idList idList
     * @param result result
     * @param <T>    T
     */
    public static <T extends TreeNode<T>> void getAllNodes(List<T> nodes,
                                                           List<Long> idList,
                                                           List<T> result) {
        if (Lists.isEmpty(idList)) {
            return;
        }
        // 获取当前节点的数据
        List<T> currentNodes = nodes.stream()
                .filter(s -> idList.contains(s.getId()))
                .collect(Collectors.toList());
        if (currentNodes.isEmpty()) {
            return;
        }
        result.addAll(currentNodes);
        // 获取父节点id
        List<Long> parentIdList = currentNodes.stream()
                .map(T::getParentId)
                .distinct()
                .collect(Collectors.toList());
        // 如果为空 或者唯一的元素为 rootId 直接返回
        if (parentIdList.isEmpty()
                || (parentIdList.size() == 1 && parentIdList.get(0).equals(Const.ROOT_PARENT_ID))) {
            return;
        }
        // 递归
        getAllNodes(nodes, parentIdList, result);
    }

    /**
     * 构建树
     *
     * @param parentNode parentNode
     * @param nodes      nodes
     * @param <T>        T
     */
    public static <T extends TreeNode<T>> void buildGroupTree(T parentNode,
                                                              List<T> nodes) {
        // 获取子节点
        List<T> childrenNodes = nodes.stream()
                .filter(s -> parentNode.getId().equals(s.getParentId()))
                .sorted(Comparator.comparing(T::getSort))
                .collect(Collectors.toList());
        if (childrenNodes.isEmpty()) {
            return;
        }
        parentNode.setChildren(childrenNodes);
        // 遍历子节点
        for (T childrenNode : childrenNodes) {
            buildGroupTree(childrenNode, nodes);
        }
    }

}
