/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.tag.TagDTO;
import com.orion.visor.module.infra.enums.TagTypeEnum;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 标签引用 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
public interface TagRelApi {

    /**
     * 创建标签引用 只新增
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void addTagRel(TagTypeEnum type, Long relId, List<Long> tagIdList);

    /**
     * 设置标签引用 先删除后新增
     *
     * @param type      type
     * @param relId     relId
     * @param tagIdList tagIdList
     */
    void setTagRel(TagTypeEnum type, Long relId, List<Long> tagIdList);

    /**
     * 获取引用 tag
     *
     * @param type  type
     * @param relId relId
     * @return tag
     */
    Future<List<TagDTO>> getRelTagsAsync(TagTypeEnum type, Long relId);

    /**
     * 获取引用 tag
     *
     * @param type      type
     * @param relIdList relIdList
     * @return tag
     */
    List<List<TagDTO>> getRelTags(TagTypeEnum type, List<Long> relIdList);

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
     */
    void deleteRelId(TagTypeEnum type, Long relId);

    /**
     * 通过 relIdList 删除
     *
     * @param type      type
     * @param relIdList relIdList
     */
    void deleteRelIdList(TagTypeEnum type, List<Long> relIdList);

}
