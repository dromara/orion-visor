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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.dto.TagCacheDTO;

import java.util.List;

/**
 * 标签引用 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
public interface TagRelService {

    /**
     * 创建标签引用
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void addTagRel(String type, Long relId, List<Long> tagIdList);

    /**
     * 设置标签引用
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void setTagRel(String type, Long relId, List<Long> tagIdList);

    /**
     * 获取引用 tag
     *
     * @param type  type
     * @param relId relId
     * @return tag
     */
    List<TagCacheDTO> getRelTags(String type, Long relId);

    /**
     * 获取引用 tag
     *
     * @param type      type
     * @param relIdList relIdList
     * @return tag
     */
    List<List<TagCacheDTO>> getRelTags(String type, List<Long> relIdList);

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagId tagId
     * @return rel
     */
    List<Long> getRelIdByTagId(Long tagId);

    /**
     * 查询 tag 关联的所有 id
     *
     * @param tagIdList tagIdList
     * @return rel
     */
    List<Long> getRelIdByTagId(List<Long> tagIdList);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteRelId(String type, Long relId);

    /**
     * 通过 relIdList 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteRelIdList(String type, List<Long> relIdList);

}
