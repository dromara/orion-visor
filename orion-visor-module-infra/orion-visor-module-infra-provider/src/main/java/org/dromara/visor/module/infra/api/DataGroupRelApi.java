/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.api;

import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * 数据分组关联 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelApi {

    /**
     * 设置关联
     *
     * @param groupId   groupId
     * @param relIdList relIdList
     */
    void updateGroupRel(Long groupId, List<Long> relIdList);

    /**
     * 设置关联
     *
     * @param type        type
     * @param groupIdList groupIdList
     * @param relId       relId
     */
    void updateRelGroup(DataGroupTypeEnum type, List<Long> groupIdList, Long relId);

    /**
     * 添加关联
     *
     * @param type    type
     * @param groupId groupId
     * @param relId   relId
     */
    void addGroupRel(DataGroupTypeEnum type, Long groupId, Long relId);

    /**
     * 批量添加关联
     *
     * @param type            type
     * @param groupRelListMap groupRelListMap
     */
    void addGroupRel(DataGroupTypeEnum type, Map<Long, List<Long>> groupRelListMap);

    /**
     * 通过 type 查询 relId 缓存
     * <p>
     * groupId - relId
     *
     * @param type type
     * @return rows
     */
    Map<Long, Set<Long>> getGroupRelList(DataGroupTypeEnum type);

    /**
     * 通过 groupId 查询 relId 缓存
     *
     * @param type    type
     * @param groupId groupId
     * @return relId
     */
    Set<Long> getGroupRelIdByGroupId(DataGroupTypeEnum type, Long groupId);

    /**
     * 通过 relId 查询 groupId
     *
     * @param type  type
     * @param relId relId
     * @return groupId
     */
    Future<Set<Long>> getGroupIdByRelIdAsync(DataGroupTypeEnum type, Long relId);

    /**
     * 删除数据分组关联
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(DataGroupTypeEnum type, Long relId);

    /**
     * 批量删除数据分组关联
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(DataGroupTypeEnum type, List<Long> relIdList);

    /**
     * 批量删除数据分组关联
     *
     * @param type        type
     * @param groupIdList groupIdList
     * @return effect
     */
    Integer deleteByGroupIdList(DataGroupTypeEnum type, List<Long> groupIdList);

}
